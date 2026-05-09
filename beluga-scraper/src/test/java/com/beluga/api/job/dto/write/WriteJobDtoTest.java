package com.x.scrape.api.job.dto.write;

import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.beluga.test_utils.TestReflectionUtility.assertAnnotationPresentOnField;

class WriteJobDtoTest {
	
	@ParameterizedTest
	@ValueSource(
			strings = {
					"storage",
					"execution",
					"scraping"
			}
	)
	void shouldHaveNotNullOnFields(final String fieldName) {
		assertAnnotationPresentOnField(
				fieldName,
				WriteJobDefinitionDto.class,
				NotNull.class
		);
	}
}