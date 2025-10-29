package com.x.scrape.properties.persistence.validation;

import com.x.scrape.properties.persistence.PersistenceProperties;
import jakarta.validation.ConstraintValidatorContext;
import org.instancio.Instancio;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static com.x.scrape.properties.persistence.PersistenceType.*;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HasCorrectPersistenceStoreValidatorTest {
	
	@Mock(answer = RETURNS_DEEP_STUBS)
	private ConstraintValidatorContext context;
	
	private final HasCorrectPersistenceStoreValidator validator = new HasCorrectPersistenceStoreValidator();
	
	@ParameterizedTest
	@MethodSource
	void shouldBeValid(final PersistenceProperties persistenceProperties) {
		assertTrue(validator.isValid(persistenceProperties, context));
	}
	
	static Stream<Arguments> shouldBeValid() {
		return Stream.of(
				Instancio.of(PersistenceProperties.class)
						.ignore(field(PersistenceProperties::getFileSystem))
						.ignore(field(PersistenceProperties::getPostgresql))
						.set(field(PersistenceProperties::getType), S3)
						.create(),
				Instancio.of(PersistenceProperties.class)
						.ignore(field(PersistenceProperties::getS3))
						.ignore(field(PersistenceProperties::getPostgresql))
						.set(field(PersistenceProperties::getType), FILE_SYSTEM)
						.create(),
				Instancio.of(PersistenceProperties.class)
						.ignore(field(PersistenceProperties::getS3))
						.ignore(field(PersistenceProperties::getFileSystem))
						.set(field(PersistenceProperties::getType), POSTGRESQL)
						.create()
		).map(Arguments::of);
	}
	
	@ParameterizedTest
	@MethodSource
	void shouldBeInvalid(final PersistenceProperties properties,
	                     final String expectedMessage) {
		assertFalse(validator.isValid(properties, context));
		verify(context).buildConstraintViolationWithTemplate(expectedMessage);
	}
	
	static Stream<Arguments> shouldBeInvalid() {
		return Stream.of(
				Arguments.of(
						Instancio.of(PersistenceProperties.class)
								.ignore(field(PersistenceProperties::getFileSystem))
								.ignore(field(PersistenceProperties::getS3))
								.set(field(PersistenceProperties::getType), S3)
								.create(),
						"When S3 is used as persistence type, then the s3 properties should be configured"
				),
				Arguments.of(
						Instancio.of(PersistenceProperties.class)
								.ignore(field(PersistenceProperties::getS3))
								.ignore(field(PersistenceProperties::getFileSystem))
								.set(field(PersistenceProperties::getType), FILE_SYSTEM)
								.create(),
						"When FILE_SYSTEM is used as persistence type, then the file-system properties should be configured"
				),
				Arguments.of(
						Instancio.of(PersistenceProperties.class)
								.ignore(field(PersistenceProperties::getS3))
								.ignore(field(PersistenceProperties::getPostgresql))
								.set(field(PersistenceProperties::getType), POSTGRESQL)
								.create(),
						"When POSTGRESQL is used as persistence type, then the postgresql properties should be configured"
				)
		);
	}
}