package com.x.scrape.api.job.dto.base;

import jakarta.validation.constraints.Positive;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.x.scrape.test_utils.TestReflectionUtility.assertAnnotationPresentOnField;

class BaseExecutionDefinitionDtoTest {
	
	@ParameterizedTest
	@ValueSource(
			strings = {
					"workers",
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