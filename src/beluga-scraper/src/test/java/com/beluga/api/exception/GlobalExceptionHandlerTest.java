package com.beluga.api.exception;

import com.beluga.api.exception.dto.ErrorResponseDto;
import com.beluga.api.exception.factory.ErrorResponseDtoFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private ErrorResponseDtoFactory errorResponseDtoFactory;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void shouldHandleMethodArgumentNotValidException() {
        final MethodArgumentNotValidException exception = mock();

        final ErrorResponseDto expectedResponseDto = mock();
        when(errorResponseDtoFactory.create(exception, BAD_REQUEST)).thenReturn(expectedResponseDto);

        final ResponseEntity<ErrorResponseDto> responseEntity = globalExceptionHandler.handle(exception);

        assertSame(expectedResponseDto, responseEntity.getBody());
        assertEquals(400, responseEntity.getStatusCode().value());
    }

}