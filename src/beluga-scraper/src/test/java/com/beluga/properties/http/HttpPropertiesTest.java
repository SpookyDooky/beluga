package com.beluga.properties.http;

import com.beluga.properties.http.validation.ValidMaxBodySize;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.validation.annotation.Validated;

import java.util.stream.Stream;

import static com.beluga.test_utils.TestReflectionUtility.assertAnnotationPresentOnClass;
import static com.beluga.test_utils.TestReflectionUtility.assertAnnotationPresentOnField;

class HttpPropertiesTest {

	@ParameterizedTest
	@MethodSource
	void shouldHaveNotNullAnnotationOnField(final String fieldName) {
		assertAnnotationPresentOnField(fieldName, HttpProperties.class, NotNull.class);
	}

	static Stream<Arguments> shouldHaveNotNullAnnotationOnField() {
		return Stream.of(
				"timeout",
				"maxBodySize"
		).map(Arguments::of);
	}

	@Test
	void shouldHaveValidMaxBodySizeAnnotationOnMaxBodySize() {
		assertAnnotationPresentOnField("maxBodySize", HttpProperties.class, ValidMaxBodySize.class);
	}

	@Test
	void shouldHaveValidatedAnnotationOnClass() {
		assertAnnotationPresentOnClass(HttpProperties.class, Validated.class);
	}
}
