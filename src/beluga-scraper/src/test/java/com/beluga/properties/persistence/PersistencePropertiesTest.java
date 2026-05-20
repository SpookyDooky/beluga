package com.beluga.properties.persistence;

import com.beluga.properties.persistence.validation.HasCorrectPersistenceStore;
import com.beluga.properties.persistence.validation.HasOnlyOnePersistenceStore;
import jakarta.validation.constraints.NotEmpty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.validation.annotation.Validated;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

import static com.beluga.properties.persistence.PersistenceType.SQLITE;
import static com.beluga.test_utils.TestReflectionUtility.assertAnnotationPresentOnClass;
import static com.beluga.test_utils.TestReflectionUtility.assertAnnotationPresentOnField;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PersistencePropertiesTest {
	
	@Test
	void shouldHaveDefaultSqliteAsType() {
		assertEquals(SQLITE, new PersistenceProperties().getType());
	}
	
	@ParameterizedTest
	@MethodSource
	void shouldHaveNotEmptyAnnotationOnField(final String fieldName) {
		assertAnnotationPresentOnField(fieldName, PersistenceProperties.class, NotEmpty.class);
	}
	
	static Stream<Arguments> shouldHaveNotEmptyAnnotationOnField() {
		return Stream.of(
				"type"
		).map(Arguments::of);
	}
	
	@ParameterizedTest
	@MethodSource
	void shouldHaveAnnotationOnClass(final Class<? extends Annotation> clazz) {
		assertAnnotationPresentOnClass(PersistenceProperties.class, clazz);
	}
	
	static Stream<Arguments> shouldHaveAnnotationOnClass() {
		return Stream.of(
				HasCorrectPersistenceStore.class,
				HasOnlyOnePersistenceStore.class,
				Validated.class
		).map(Arguments::of);
	}
}