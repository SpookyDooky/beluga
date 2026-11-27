package com.beluga.http.service;

import com.beluga.activity_logging.activitiy.RequestActivity;
import com.beluga.activity_logging.event.ActivityEvent;
import com.beluga.http.model.HttpResult;
import com.beluga.logging.CloseableContext;
import com.beluga.logging.ContextKeys;
import com.beluga.logging.ContextLogger;
import com.beluga.properties.BelugaScraperProperties;
import com.beluga.properties.http.HttpProperties;
import com.beluga.util.TimingService;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.beluga.logging.ContextKeys.HTTP_METHOD;

@Service
public class HttpService {

    /**
     * Covers connecting and reading the complete response.
     */
    private static final int MAX_BODY_SIZE = 10 * 1024 * 1024;
    private static final String USER_AGENT = "Beluga/1.0";

    static {
        // By default jsoup sends requests with the JDK HttpClient. Its timeout then only covers the response headers, so a
        // server that stops sending the body blocks the read forever. With HttpURLConnection the timeout covers connecting
        // and reading the complete response. jsoup reads this property on every request.
        System.setProperty("jsoup.useHttpClient", "false");
    }

    private final ContextLogger logger;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final TimingService timingService;
    private final HttpProperties httpProperties;

    public HttpService(final ContextLogger logger,
                       final ApplicationEventPublisher applicationEventPublisher,
                       final TimingService timingService,
                       final BelugaScraperProperties belugaScraperProperties) {
        this.logger = logger;
        this.applicationEventPublisher = applicationEventPublisher;
        this.timingService = timingService;
        this.httpProperties = belugaScraperProperties.getHttp();
    }

    public Optional<HttpResult> get(final URL url) {
        return executeRequest(url, Method.GET);
    }

    private Optional<HttpResult> executeRequest(final URL url,
                                                final Method httpMethod) {
        final UUID timingUuid = timingService.start();

        try (final CloseableContext ignored = logger.withKeys(Map.of(
                ContextKeys.URL, url.toString(),
                HTTP_METHOD, httpMethod.name()
        ))) {
            logger.info("Executing HTTP request.");

            final Response response = Jsoup.connect(url.toString())
                    .method(httpMethod)
                    .userAgent(USER_AGENT)
                    .timeout((int) httpProperties.getTimeout().toMillis())
                    .maxBodySize((int) httpProperties.getMaxBodySizeAsBytes() + 1)
                    .ignoreContentType(true)
                    .execute();
            final byte[] body = response.bodyAsBytes();

            publishRequestActivity(url, timingUuid, true);

            if (body.length > httpProperties.getMaxBodySizeAsBytes()) {
                logger.error("The response body exceeds the maximum of " + MAX_BODY_SIZE + " bytes.");
                return Optional.empty();
            }

            return Optional.of(HttpResult.of(
                    response.statusCode(),
                    toUri(response.url()),
                    response.contentType(),
                    response.charset(),
                    body
            ));
        } catch (final Exception e) { // NOPMD
            logger.error("Failed to execute HTTP request.", e);
            publishRequestActivity(url, timingUuid, false);
            return Optional.empty();
        }
    }

    private URI toUri(final URL url) {
        try {
            return url.toURI();
        } catch (final URISyntaxException e) {
            logger.error("Failed to convert URL to URI", e);
            throw new IllegalArgumentException("Failed to convert URL to URI.", e);
        }
    }

    private void publishRequestActivity(final URL url,
                                        final UUID timingUuid,
                                        final boolean success) {
        applicationEventPublisher.publishEvent(new ActivityEvent(new RequestActivity(url, timingService.stop(timingUuid), success)));
    }
}
