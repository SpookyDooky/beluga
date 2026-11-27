package com.beluga.properties.http.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.unit.DataSize;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ValidMaxBodySizeValidatorTest {

	@Mock
	private ConstraintValidatorContext context;

	private final ValidMaxBodySizeValidator validator = new ValidMaxBodySizeValidator();

	@ParameterizedTest
	@MethodSource
	void shouldBeValid(final DataSize maxBodySize) {
		assertTrue(validator.isValid(maxBodySize, context));
	}

	static Stream<Arguments> shouldBeValid() {
		return Stream.of(
				null,
				DataSize.ofBytes(1),
				DataSize.ofMegabytes(10),
				DataSize.ofBytes(Integer.MAX_VALUE - 1L)
		).map(Arguments::of);
	}

	@ParameterizedTest
	@MethodSource
	void shouldBeInvalid(final DataSize maxBodySize) {
		assertFalse(validator.isValid(maxBodySize, context));
	}

	static Stream<Arguments> shouldBeInvalid() {
		return Stream.of(
				DataSize.ofBytes(0),
				DataSize.ofBytes(-1),
				DataSize.ofBytes(Integer.MAX_VALUE),
				DataSize.ofGigabytes(3)
		).map(Arguments::of);
	}
}
