package com.x.scrape.api.job.dto.base;

import jakarta.validation.constraints.NotEmpty;
import org.junit.jupiter.api.Test;

import static com.x.scrape.test_utils.TestReflectionUtility.assertAnnotationPresentOnField;

class BaseJobDefinitionDtoTest {
	
	@Test
	void shouldHaveNotEmptyAnnotationOnName() {
		assertAnnotationPresentOnField(
				"name",
				BaseJobDefinitionDto.class,
				NotEmpty.class
		);
	}
}