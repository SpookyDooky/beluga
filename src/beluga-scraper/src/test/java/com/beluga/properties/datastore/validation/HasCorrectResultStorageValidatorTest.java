package com.beluga.properties.datastore.validation;

import com.beluga.properties.datastore.ResultStorageProperties;
import jakarta.validation.ConstraintValidatorContext;
import org.instancio.Instancio;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static com.beluga.properties.datastore.DataStoreType.FILE_SYSTEM;
import static com.beluga.properties.datastore.DataStoreType.S3;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HasCorrectResultStorageValidatorTest {

	@Mock(answer = RETURNS_DEEP_STUBS)
	private ConstraintValidatorContext context;

	private final HasCorrectResultStorageValidator validator = new HasCorrectResultStorageValidator();

	@ParameterizedTest
	@MethodSource
	void shouldBeValid(final ResultStorageProperties resultStorageProperties) {
		assertTrue(validator.isValid(resultStorageProperties, context));
	}

	static Stream<Arguments> shouldBeValid() {
		return Stream.of(
				Instancio.of(ResultStorageProperties.class)
						.set(field(ResultStorageProperties::getType), S3)
						.create(),
				Instancio.of(ResultStorageProperties.class)
						.set(field(ResultStorageProperties::getType), FILE_SYSTEM)
						.ignore(field(ResultStorageProperties::getS3))
						.create()
		).map(Arguments::of);
	}

	@ParameterizedTest
	@MethodSource
	void shouldBeInvalid(final ResultStorageProperties properties,
	                     final String expectedMessage) {
		assertFalse(validator.isValid(properties, context));
		verify(context).buildConstraintViolationWithTemplate(expectedMessage);
	}

	static Stream<Arguments> shouldBeInvalid() {
		return Stream.of(
				Arguments.of(
						Instancio.of(ResultStorageProperties.class)
								.ignore(field(ResultStorageProperties::getS3))
								.set(field(ResultStorageProperties::getType), S3)
								.create(),
						"When S3 is used as result storage type, then the s3 properties should be configured"
				)
		);
	}
}
