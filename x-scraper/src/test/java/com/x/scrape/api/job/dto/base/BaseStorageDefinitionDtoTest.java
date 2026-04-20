package com.x.scrape.api.job.dto.base;

import com.x.scrape.api.job.dto.validation.Path;
import org.junit.jupiter.api.Test;

import static com.x.scrape.test_utils.TestReflectionUtility.assertAnnotationPresentOnField;

class BaseStorageDefinitionDtoTest {
	
	@Test
	void shouldHavePathAnnotationOnFolderField() {
		assertAnnotationPresentOnField(
				"folder",
				BaseStorageConfigurationDto.class,
				Path.class
		);
	}
}