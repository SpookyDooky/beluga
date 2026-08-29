package com.beluga.api.exception.dto;

import java.time.Instant;
import java.util.List;

public class ErrorResponseDto {

    private final Instant timestamp = Instant.now();
    private int status;
    private List<String> errors;

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(final List<String> errors) {
        this.errors = errors;
    }
}
