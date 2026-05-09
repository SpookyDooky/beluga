package com.beluga.api.job.dto.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;

@ExtendWith(MockitoExtension.class)
class PathValidatorTest {
	
	@Mock(answer = RETURNS_DEEP_STUBS)
	private ConstraintValidatorContext context;
	
	private final PathValidator validator = new PathValidator();
	
	@ParameterizedTest
	@ValueSource(
			strings = {
					"/simple",
					"/simple/longer/path"
			}
	)
	void shouldBeValid(final String path) {
		assertTrue(validator.isValid(path, context));
	}
	
	@ParameterizedTest
	@ValueSource(
			strings = {
					"simple",
					"simple/longer/path",
					"/simple/",
					""
			}
	)
	void shouldBeInvalid(final String path) {
		assertFalse(validator.isValid(path, context));
	}
	
	@Test
	void shouldBeInvalidForNull() {
		assertFalse(validator.isValid(null, context));
	}
}