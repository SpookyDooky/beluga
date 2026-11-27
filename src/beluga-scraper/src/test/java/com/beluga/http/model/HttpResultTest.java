package com.beluga.http.model;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class HttpResultTest {

    @Test
    void shouldCreateHttpResult() {
        final URI uri = URI.create("https://example.com/page");
        final byte[] body = "<html></html>".getBytes(StandardCharsets.UTF_8);

        final HttpResult httpResult = HttpResult.of(200, uri, "text/html; charset=UTF-8", body);

        assertEquals(200, httpResult.getStatusCode());
        assertEquals(uri, httpResult.getUri());
        assertEquals("text/html; charset=UTF-8", httpResult.getContentType());
        assertArrayEquals(body, httpResult.getBody());
    }

    @Test
    void shouldCreateHttpResultWithoutContentType() {
        final HttpResult httpResult = HttpResult.of(200, URI.create("https://example.com/page"), null, new byte[0]);

        assertNull(httpResult.getContentType());
    }

    @Test
    void shouldNotChangeBodyWhenOriginalArrayIsModified() {
        final byte[] body = {1, 2, 3};
        final HttpResult httpResult = HttpResult.of(200, URI.create("https://example.com/image.png"), "image/png", body);

        body[0] = 9;

        assertArrayEquals(new byte[]{1, 2, 3}, httpResult.getBody());
    }

    @Test
    void shouldNotChangeBodyWhenReturnedArrayIsModified() {
        final HttpResult httpResult = HttpResult.of(200, URI.create("https://example.com/image.png"), "image/png", new byte[]{1, 2, 3});

        final byte[] returnedBody = httpResult.getBody();
        returnedBody[0] = 9;

        assertArrayEquals(new byte[]{1, 2, 3}, httpResult.getBody());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUriIsNull() {
        assertThrows(IllegalArgumentException.class, () -> HttpResult.of(200, null, "text/html", new byte[0]));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenBodyIsNull() {
        assertThrows(IllegalArgumentException.class, () -> HttpResult.of(200, URI.create("https://example.com/page"), "text/html", null));
    }
}
