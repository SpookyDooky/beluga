package com.beluga.properties.persistence.validation;

import com.beluga.properties.persistence.PersistenceProperties;
import jakarta.validation.ConstraintValidatorContext;
import org.instancio.Instancio;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static com.beluga.properties.persistence.PersistenceType.POSTGRESQL;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class HasOnlyOnePersistenceStoreValidatorTest {
	
	@Mock
	private ConstraintValidatorContext context;
	
	private final HasOnlyOnePersistenceStoreValidator validator = new HasOnlyOnePersistenceStoreValidator();
	
	@ParameterizedTest
	@MethodSource
	void shouldBeValid(final PersistenceProperties persistenceProperties) {
		assertTrue(validator.isValid(persistenceProperties, context));
	}
	
	static Stream<Arguments> shouldBeValid() {
		return Stream.of(
				Instancio.of(PersistenceProperties.class)
						.ignore(field(PersistenceProperties::getType))
						.ignore(field(PersistenceProperties::getPostgresql))
						.create(),
				Instancio.of(PersistenceProperties.class)
						.set(field(PersistenceProperties::getType), POSTGRESQL)
						.create()
		).map(Arguments::of);
	}
	
	@ParameterizedTest
	@MethodSource
	void shouldBeInvalid(final PersistenceProperties persistenceProperties) {
		assertFalse(validator.isValid(persistenceProperties, context));
	}
	
	static Stream<Arguments> shouldBeInvalid() {
		return Stream.of(
				Instancio.of(PersistenceProperties.class)
						.ignore(field(PersistenceProperties::getType))
						.create()
		).map(Arguments::of);
	}
}