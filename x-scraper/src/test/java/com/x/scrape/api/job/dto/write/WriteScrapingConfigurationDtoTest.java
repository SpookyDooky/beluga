package com.x.scrape.api.job.dto.write;

import jakarta.validation.constraints.NotEmpty;
import org.junit.jupiter.api.Test;

import static com.x.scrape.test_utils.TestReflectionUtility.assertAnnotationPresentOnField;

class WriteScrapingConfigurationDtoTest {
	
	@Test
	void shouldHaveNotEmptyOnDataPoints() {
		assertAnnotationPresentOnField(
				"dataPoints",
				WriteScrapingConfigurationDto.class,
				NotEmpty.class
		);
	}
}