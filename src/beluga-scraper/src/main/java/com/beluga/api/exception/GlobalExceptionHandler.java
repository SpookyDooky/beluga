package com.beluga.api.exception;

import com.beluga.api.exception.dto.ErrorResponseDto;
import com.beluga.api.exception.factory.ErrorResponseDtoFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorResponseDtoFactory errorResponseDtoFactory;

    public GlobalExceptionHandler(final ErrorResponseDtoFactory errorResponseDtoFactory) {
        this.errorResponseDtoFactory = errorResponseDtoFactory;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handle(final MethodArgumentNotValidException methodArgumentNotValidException) {
        return ResponseEntity.badRequest()
                .body(errorResponseDtoFactory.create(methodArgumentNotValidException, BAD_REQUEST));
    }
}
