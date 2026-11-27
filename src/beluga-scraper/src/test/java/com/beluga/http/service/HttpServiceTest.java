package com.beluga.http.service;

import com.beluga.activity_logging.activitiy.RequestActivity;
import com.beluga.activity_logging.event.ActivityEvent;
import com.beluga.http.model.HttpResult;
import com.beluga.logging.ContextLogger;
import com.beluga.properties.http.HttpProperties;
import com.beluga.util.TimingService;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import mockwebserver3.RecordedRequest;
import okio.Buffer;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.unit.DataSize;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HttpServiceTest {

	private static final String PAGE = "<html><head><title>Test page</title></head><body><p>content</p></body></html>";
	private static final int MAX_BODY_SIZE = 100;
	private static final Duration TIMEOUT = Duration.ofSeconds(1);

	@Mock
	private ContextLogger logger;
	@Mock
	private ApplicationEventPublisher applicationEventPublisher;

	@Captor
	private ArgumentCaptor<ActivityEvent> activityEventCaptor;

	private MockWebServer server;
	private HttpService httpService;

	@BeforeEach
	void setUp() throws IOException {
		server = new MockWebServer();
		server.start();

		final HttpProperties httpProperties = Instancio.of(HttpProperties.class)
				.set(field(HttpProperties::getTimeout), TIMEOUT)
				.set(field(HttpProperties::getMaxBodySize), DataSize.ofBytes(MAX_BODY_SIZE))
				.create();

		httpService = new HttpService(logger, applicationEventPublisher, new TimingService(), httpProperties);
	}

	@AfterEach
	void tearDown() {
		server.close();
	}

	@Test
	void shouldFetchPage() throws Exception {
		server.enqueue(createResponse(200));
		final URL url = server.url("/page").url();

		final Optional<HttpResult> httpResult = httpService.get(url);

		assertTrue(httpResult.isPresent());
		assertEquals(200, httpResult.get().getStatusCode());
		assertEquals(url.toURI(), httpResult.get().getUri());
		assertEquals("text/html; charset=utf-8", httpResult.get().getContentType());
		assertEquals("utf-8", httpResult.get().getCharset());
		assertEquals(PAGE, new String(httpResult.get().getBody(), StandardCharsets.UTF_8));
		assertRequestActivityPublished(url, true);
	}

	@Test
	void shouldReturnFinalUriWhenFollowingRedirects() throws Exception {
		server.enqueue(new MockResponse.Builder()
				.code(302)
				.addHeader("Location", "/page")
				.build());
		server.enqueue(createResponse(200));
		final URL url = server.url("/moved").url();

		final Optional<HttpResult> httpResult = httpService.get(url);

		assertTrue(httpResult.isPresent());
		assertEquals(200, httpResult.get().getStatusCode());
		assertEquals(server.url("/page").url().toURI(), httpResult.get().getUri());
		assertRequestActivityPublished(url, true);
	}

	@ParameterizedTest
	@ValueSource(ints = {
			400,
			404,
			500,
			503
	})
	void shouldNotReturnResultForErrorStatus(final int status) {
		server.enqueue(createResponse(status));
		final URL url = server.url("/page").url();

		final Optional<HttpResult> httpResult = httpService.get(url);

		assertTrue(httpResult.isEmpty());
	}

	@Test
	void shouldReturnResultWithoutContentTypeWhenHeaderIsMissing() {
		server.enqueue(new MockResponse.Builder()
				.body("file content")
				.build());
		final URL url = server.url("/file").url();

		final Optional<HttpResult> httpResult = httpService.get(url);

		assertTrue(httpResult.isPresent());
		assertNull(httpResult.get().getContentType());
		assertNull(httpResult.get().getCharset());
		assertEquals("file content", new String(httpResult.get().getBody(), StandardCharsets.UTF_8));
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"text/html",
			"text/html; charset=does-not-exist",
			"text/html; charset="
	})
	void shouldReturnNoCharsetWhenContentTypeHasNoSupportedCharset(final String contentType) {
		server.enqueue(new MockResponse.Builder()
				.addHeader("Content-Type", contentType)
				.body(PAGE)
				.build());
		final URL url = server.url("/page").url();

		final Optional<HttpResult> httpResult = httpService.get(url);

		assertTrue(httpResult.isPresent());
		assertNull(httpResult.get().getCharset());
	}

	@Test
	void shouldReturnResultForContentTypeThatIsNotHtml() {
		final Buffer image = new Buffer().write(new byte[]{(byte) 0x89, 'P', 'N', 'G'});
		server.enqueue(new MockResponse.Builder()
				.addHeader("Content-Type", "image/png")
				.body(image)
				.build());
		final URL url = server.url("/image.png").url();

		final Optional<HttpResult> httpResult = httpService.get(url);

		assertTrue(httpResult.isPresent());
		assertEquals("image/png", httpResult.get().getContentType());
		assertArrayEquals(new byte[]{(byte) 0x89, 'P', 'N', 'G'}, httpResult.get().getBody());
	}

	@Test
	void shouldSendUserAgent() throws Exception {
		server.enqueue(createResponse(200));
		final URL url = server.url("/page").url();

		httpService.get(url);

		final RecordedRequest recordedRequest = server.takeRequest();
		assertEquals("Beluga/1.0", recordedRequest.getHeaders().get("User-Agent"));
	}

	@Test
	void shouldReturnEmptyWhenContentLengthExceedsMaximumBodySize() {
		server.enqueue(new MockResponse.Builder()
				.body("a".repeat(MAX_BODY_SIZE + 1))
				.build());
		final URL url = server.url("/large").url();

		final Optional<HttpResult> httpResult = httpService.get(url);

		assertTrue(httpResult.isEmpty());
		assertRequestActivityPublished(url, true);
	}

	@Test
	void shouldReturnEmptyWhenChunkedBodyExceedsMaximumBodySize() {
		server.enqueue(new MockResponse.Builder()
				.chunkedBody("a".repeat(MAX_BODY_SIZE + 1), 10)
				.build());
		final URL url = server.url("/large").url();

		final Optional<HttpResult> httpResult = httpService.get(url);

		assertTrue(httpResult.isEmpty());
		assertRequestActivityPublished(url, true);
	}

	@Test
	void shouldReturnResultWhenBodyHasMaximumBodySize() {
		server.enqueue(new MockResponse.Builder()
				.chunkedBody("a".repeat(MAX_BODY_SIZE), 10)
				.build());
		final URL url = server.url("/large").url();

		final Optional<HttpResult> httpResult = httpService.get(url);

		assertTrue(httpResult.isPresent());
		assertEquals(MAX_BODY_SIZE, httpResult.get().getBody().length);
	}

	@Test
	void shouldReturnEmptyWhenBodyIsNotReceivedWithinTimeout() {
		server.enqueue(new MockResponse.Builder()
				.body(PAGE)
				.bodyDelay(TIMEOUT.toMillis() * 3, TimeUnit.MILLISECONDS)
				.build());
		final URL url = server.url("/slow").url();

		final Optional<HttpResult> httpResult = httpService.get(url);

		assertTrue(httpResult.isEmpty());
		assertRequestActivityPublished(url, false);
	}

	@Test
	void shouldReturnEmptyWhenBodyIsNotCompleteWithinTimeout() {
		server.enqueue(new MockResponse.Builder()
				.body("a".repeat(20))
				.throttleBody(1, 200, TimeUnit.MILLISECONDS)
				.build());
		final URL url = server.url("/trickle").url();

		final Optional<HttpResult> httpResult = httpService.get(url);

		assertTrue(httpResult.isEmpty());
		assertRequestActivityPublished(url, false);
	}

	@Test
	void shouldReturnEmptyWhenConnectionIsRefused() {
		final URL url = closedServerUrl();

		final Optional<HttpResult> httpResult = httpService.get(url);

		assertTrue(httpResult.isEmpty());
		assertRequestActivityPublished(url, false);
	}

	@Test
	void shouldEncodeUrlWithCharactersThatAreNotAllowedInUri() throws Exception {
		server.enqueue(createResponse(200));
		final URL url = new URL(server.url("/").url(), "page with spaces");

		final Optional<HttpResult> httpResult = httpService.get(url);

		assertTrue(httpResult.isPresent());
		assertEquals("/page%20with%20spaces", server.takeRequest().getTarget());
		assertEquals(server.url("/page%20with%20spaces").url().toURI(), httpResult.get().getUri());
	}

	@Test
	void shouldReturnDecompressedBodyForGzipResponse() throws Exception {
		final Buffer compressedBody = new Buffer();
		try (final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(compressedBody.outputStream())) {
			gzipOutputStream.write(PAGE.getBytes(StandardCharsets.UTF_8));
		}
		server.enqueue(new MockResponse.Builder()
				.addHeader("Content-Encoding", "gzip")
				.body(compressedBody)
				.build());
		final URL url = server.url("/page").url();

		final Optional<HttpResult> httpResult = httpService.get(url);

		assertTrue(httpResult.isPresent());
		assertEquals(PAGE, new String(httpResult.get().getBody(), StandardCharsets.UTF_8));
		assertEquals("gzip", server.takeRequest().getHeaders().get("Accept-Encoding"));
	}

	@Test
	void shouldUseHttpUrlConnectionSoTheTimeoutCoversTheBody() {
		assertEquals("false", System.getProperty("jsoup.useHttpClient"));
	}

	@Test
	void shouldReturnEmptyWhenSchemeIsNotSupported() throws Exception {
		final URL url = new URL("ftp://localhost/file");

		final Optional<HttpResult> httpResult = httpService.get(url);

		assertTrue(httpResult.isEmpty());
		assertRequestActivityPublished(url, false);
	}

	private MockResponse createResponse(final int status) {
		return new MockResponse.Builder()
				.code(status)
				.addHeader("Content-Type", "text/html; charset=utf-8")
				.body(PAGE)
				.build();
	}

	/**
	 * Creates a URL for a server that no longer runs, so that connecting to it gets refused.
	 */
	private URL closedServerUrl() {
		final URL url = server.url("/page").url();
		server.close();

		return url;
	}

	private void assertRequestActivityPublished(final URL url,
	                                            final boolean success) {
		verify(applicationEventPublisher).publishEvent(activityEventCaptor.capture());

		final RequestActivity activity = assertInstanceOf(RequestActivity.class, activityEventCaptor.getValue().getActivity());
		assertEquals(url.toString(), activity.getUrl().toString());
		assertEquals(success, activity.isSuccess());
		assertTrue(activity.getDuration() >= 0);
	}
}
