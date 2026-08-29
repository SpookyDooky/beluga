package com.beluga.api.exception.factory;

import com.beluga.api.exception.dto.ErrorResponseDto;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class ErrorResponseDtoFactoryTest {

    private final ErrorResponseDtoFactory errorResponseDtoFactory = new ErrorResponseDtoFactory();

    @Test
    void shouldCreateForMethodArgumentNotValidException() {
        final MethodArgumentNotValidException methodArgumentNotValidException = mock();
        final FieldError fieldError = Instancio.create(FieldError.class);
        when(methodArgumentNotValidException.getFieldErrors()).thenReturn(List.of(fieldError));

        final ErrorResponseDto errorResponseDto = errorResponseDtoFactory.create(methodArgumentNotValidException, BAD_REQUEST);

        assertEquals(400, errorResponseDto.getStatus());
        assertEquals(fieldError.getDefaultMessage(), errorResponseDto.getErrors().getFirst());
    }

    @Test
    void shouldCreateForIllegalArgumentException() {
        final IllegalArgumentException illegalArgumentException = mock();
        final String message = "message";
        when(illegalArgumentException.getMessage()).thenReturn(message);

        final ErrorResponseDto errorResponseDto = errorResponseDtoFactory.create(illegalArgumentException, BAD_REQUEST);

        assertEquals(400, errorResponseDto.getStatus());
        assertTrue(errorResponseDto.getErrors().contains(message));
    }
}