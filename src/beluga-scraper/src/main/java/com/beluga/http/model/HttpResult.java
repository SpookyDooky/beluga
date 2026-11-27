package com.beluga.http.model;

import java.net.URI;

public class HttpResult {

    private int statusCode;
    private URI uri;
    private String contentType;
    private byte[] body;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(final int statusCode) {
        this.statusCode = statusCode;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(final URI uri) {
        this.uri = uri;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(final byte[] body) {
        this.body = body;
    }
}
