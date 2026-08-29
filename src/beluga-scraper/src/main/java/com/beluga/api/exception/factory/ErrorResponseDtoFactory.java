package com.beluga.api.exception.factory;

import com.beluga.api.exception.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

@Component
public class ErrorResponseDtoFactory {

    public ErrorResponseDto create(final MethodArgumentNotValidException methodArgumentNotValidException,
                                   final HttpStatus httpStatus) {
        final ErrorResponseDto errorResponse = new ErrorResponseDto();

        errorResponse.setStatus(httpStatus.value());
        errorResponse.setErrors(extractErrors(methodArgumentNotValidException));

        return errorResponse;
    }

    private List<String> extractErrors(final MethodArgumentNotValidException methodArgumentNotValidException) {
        return methodArgumentNotValidException.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
    }

    public ErrorResponseDto create(final IllegalArgumentException illegalArgumentException,
                                   final HttpStatus httpStatus) {
        final ErrorResponseDto errorResponse = new ErrorResponseDto();

        errorResponse.setStatus(httpStatus.value());
        errorResponse.setErrors(List.of(illegalArgumentException.getMessage()));

        return errorResponse;
    }
}
