package com.x.scrape.properties.persistence;

import jakarta.validation.constraints.NotEmpty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.x.scrape.test_utils.TestReflectionUtility.assertAnnotationPresentOnField;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileSystemPersistencePropertiesTest {

	@ParameterizedTest
	@MethodSource
	void shouldHaveNotEmptyAnnotationOnField(final String fieldName) {
		assertAnnotationPresentOnField(fieldName, FileSystemPersistenceProperties.class, NotEmpty.class);
	}
	
	static Stream<Arguments> shouldHaveNotEmptyAnnotationOnField() {
		return Stream.of(
			"folder"
		).map(Arguments::of);
	}
	
	@Test
	void shouldRemoveTrailingSlashFromFolder() {
		final FileSystemPersistenceProperties properties = new FileSystemPersistenceProperties();
		properties.setFolder("test/");
		
		assertEquals("test", properties.getFolder());
	}
}