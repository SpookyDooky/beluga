package com.x.scrape.properties.persistence;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.x.scrape.test_utils.TestReflectionUtility.assertAnnotationPresentOnField;
import static org.junit.jupiter.api.Assertions.assertEquals;

class S3PersistencePropertiesTest {
	
	@ParameterizedTest
	@MethodSource
	void shouldHaveNotNullOnField(final String fieldName) {
		assertAnnotationPresentOnField(fieldName, S3PersistenceProperties.class, NotNull.class);
	}
	
	static Stream<Arguments> shouldHaveNotNullOnField() {
		return Stream.of(
				"host",
				"accessKey",
				"secretKey",
				"bucket",
				"region"
		).map(Arguments::of);
	}
	
	@ParameterizedTest
	@MethodSource
	void shouldHaveNotEmptyOnField(final String fieldName) {
		assertAnnotationPresentOnField(fieldName, S3PersistenceProperties.class, NotEmpty.class);
	}
	
	static Stream<Arguments> shouldHaveNotEmptyOnField() {
		return Stream.of(
				"folder"
		).map(Arguments::of);
	}
	
	@Test
	void shouldStripTrailingSlashFromFolder() {
		final S3PersistenceProperties properties = new S3PersistenceProperties();
		properties.setFolder("test/");
		
		assertEquals("test", properties.getFolder());
	}
}