package com.beluga.api.job.dto.base;

import jakarta.validation.constraints.Positive;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.beluga.test_utils.TestReflectionUtility.assertAnnotationPresentOnField;

class BaseExecutionDefinitionDtoTest {
	
	@ParameterizedTest
	@ValueSource(
			strings = {
					"tasksPerSecond"
			}
	)
	void shouldHavePositiveAnnotationOnFields(final String fieldName) {
		assertAnnotationPresentOnField(
				fieldName,
				BaseExecutionConfigurationDto.class,
				Positive.class
		);
	}
}