package com.beluga.api.job.dto.base;

import com.beluga.api.job.dto.validation.Path;
import org.junit.jupiter.api.Test;

import static com.beluga.test_utils.TestReflectionUtility.assertAnnotationPresentOnField;

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