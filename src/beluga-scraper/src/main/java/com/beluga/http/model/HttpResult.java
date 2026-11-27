package com.beluga.http.model;

import java.net.URI;
import java.util.Arrays;

/**
 * The response to an HTTP request whose body has been read completely.
 */
public class HttpResult {

    private final int statusCode;
    private final URI uri;
    private final String contentType;
    private final String charset;
    private final byte[] body;

    private HttpResult(final int statusCode,
                       final URI uri,
                       final String contentType,
                       final String charset,
                       final byte[] body) {
        this.statusCode = statusCode;
        this.uri = uri;
        this.contentType = contentType;
        this.charset = charset;
        this.body = body;
    }

    /**
     * Creates a {@link HttpResult}.
     *
     * @param statusCode  the HTTP status code of the response.
     * @param uri         the final {@link URI} of the response, after following redirects.
     * @param contentType the value of the Content-Type header, or null when the response did not include it.
     * @param charset     the charset from the Content-Type header, or null when it is missing or not supported.
     * @param body        the response body, which is copied.
     * @return the {@link HttpResult}.
     * @throws IllegalArgumentException thrown when the uri or body is null.
     */
    public static HttpResult of(final int statusCode,
                                final URI uri,
                                final String contentType,
                                final String charset,
                                final byte[] body) {
        validateNotNull("uri", uri);
        validateNotNull("body", body);

        return new HttpResult(
                statusCode,
                uri,
                contentType,
                charset,
                Arrays.copyOf(body, body.length)
        );
    }

    private static void validateNotNull(final String propertyName,
                                        final Object value) {
        if (value == null) {
            throw new IllegalArgumentException(propertyName + " must not be null.");
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public URI getUri() {
        return uri;
    }

    public String getContentType() {
        return contentType;
    }

    public String getCharset() {
        return charset;
    }

    /**
     * Returns a copy of the body, so the {@link HttpResult} can not be changed through it.
     *
     * @return a copy of the response body.
     */
    public byte[] getBody() {
        return Arrays.copyOf(body, body.length);
    }
}
