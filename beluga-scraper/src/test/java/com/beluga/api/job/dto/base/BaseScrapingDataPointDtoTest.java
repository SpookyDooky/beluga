package com.beluga.api.job.dto.base;

import jakarta.validation.constraints.NotEmpty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.beluga.model.job_definition.configuration.scraping_configuration.DataPointType.TEXT;
import static com.beluga.test_utils.TestReflectionUtility.assertAnnotationPresentOnField;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BaseScrapingDataPointDtoTest {
	
	@ParameterizedTest
	@ValueSource(
			strings = {
					"selector",
					"propertyName"
			}
	)
	void shouldHaveNotEmptyOnFields(final String fieldName) {
		assertAnnotationPresentOnField(
				fieldName,
				BaseScrapingDataPointDto.class,
				NotEmpty.class
		);
	}
	
	@Test
	void shouldHaveTextAsDefaultType() {
		final BaseScrapingDataPointDto dto = new BaseScrapingDataPointDtoImpl();
		
		assertEquals(TEXT, dto.getType());
	}
	
	static class BaseScrapingDataPointDtoImpl extends BaseScrapingDataPointDto {
	
	}
}