package com.beluga.http;

import com.beluga.activity_logging.activitiy.RequestActivity;
import com.beluga.activity_logging.event.ActivityEvent;
import com.beluga.logging.ContextLogger;
import com.beluga.util.TimingService;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import org.jsoup.nodes.Document;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HttpServiceTest {

	private static final String PAGE = "<html><head><title>Test page</title></head><body><p>content</p></body></html>";

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

		httpService = new HttpService(logger, applicationEventPublisher, new TimingService());
	}

	@AfterEach
	void tearDown() {
		server.close();
	}

	@Test
	void shouldRetrievePageAsDocument() {
		server.enqueue(createResponse(200));
		final URL url = server.url("/page").url();

		final Optional<Document> document = httpService.retrievePageAsDocument(url);

		assertTrue(document.isPresent());
		assertEquals("Test page", document.get().title());
		assertEquals("content", document.get().body().text());
		assertRequestActivityPublished(url, true);
	}

	@Test
	void shouldFollowRedirectsWhenRetrievingDocument() {
		server.enqueue(new MockResponse.Builder()
				.code(302)
				.addHeader("Location", "/page")
				.build());
		server.enqueue(createResponse(200));
		final URL url = server.url("/moved").url();

		final Optional<Document> document = httpService.retrievePageAsDocument(url);

		assertTrue(document.isPresent());
		assertEquals("Test page", document.get().title());
		assertRequestActivityPublished(url, true);
	}

	@ParameterizedTest
	@ValueSource(ints = {400, 404, 500, 503})
	void shouldReturnEmptyDocumentForErrorStatus(final int status) {
		server.enqueue(createResponse(status));
		final URL url = server.url("/page").url();

		final Optional<Document> document = httpService.retrievePageAsDocument(url);

		assertTrue(document.isEmpty());
		assertRequestActivityPublished(url, false);
	}

	@Test
	void shouldReturnEmptyDocumentForUnsupportedContentType() {
		server.enqueue(new MockResponse.Builder()
				.addHeader("Content-Type", "application/json")
				.body("{}")
				.build());
		final URL url = server.url("/data").url();

		final Optional<Document> document = httpService.retrievePageAsDocument(url);

		assertTrue(document.isEmpty());
		assertRequestActivityPublished(url, false);
	}

	@Test
	void shouldReturnEmptyDocumentWhenConnectionIsRefused() {
		final URL url = closedServerUrl();

		final Optional<Document> document = httpService.retrievePageAsDocument(url);

		assertTrue(document.isEmpty());
		assertRequestActivityPublished(url, false);
	}

	@Test
	void shouldGetContentAsStream() throws IOException {
		server.enqueue(new MockResponse.Builder()
				.body("file content")
				.build());
		final URL url = server.url("/file").url();

		try (final InputStream inputStream = httpService.get(url)) {
			assertEquals("file content", new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));
		}

		assertRequestActivityPublished(url, true);
	}

	@ParameterizedTest
	@ValueSource(ints = {404, 500})
	void shouldThrowWhenGettingContentReturnsErrorStatus(final int status) {
		server.enqueue(new MockResponse.Builder()
				.code(status)
				.build());
		final URL url = server.url("/file").url();

		final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> httpService.get(url));

		assertEquals("Could not get URL", exception.getMessage());
		assertInstanceOf(IOException.class, exception.getCause());
		assertRequestActivityPublished(url, false);
	}

	@Test
	void shouldThrowWhenGettingContentAndConnectionIsRefused() {
		final URL url = closedServerUrl();

		final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> httpService.get(url));

		assertInstanceOf(IOException.class, exception.getCause());
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
