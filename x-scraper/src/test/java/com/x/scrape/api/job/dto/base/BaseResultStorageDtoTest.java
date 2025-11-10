package com.x.scrape.api.job.dto.base;

import com.x.scrape.api.job.dto.validation.Path;
import org.junit.jupiter.api.Test;

import static com.x.scrape.test_utils.TestReflectionUtility.assertAnnotationPresentOnField;

class BaseResultStorageDtoTest {
	
	@Test
	void shouldHavePathAnnotationOnFolderField() {
		assertAnnotationPresentOnField(
				"folder",
				BaseResultStorageDto.class,
				Path.class
		);
	}
}