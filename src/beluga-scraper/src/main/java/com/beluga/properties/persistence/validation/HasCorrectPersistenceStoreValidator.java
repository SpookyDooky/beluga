package com.beluga.properties.persistence.validation;

import com.beluga.properties.persistence.PersistenceProperties;
import com.beluga.properties.persistence.PersistenceType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates if the configured {@link PersistenceType} is compatible with the actual
 * configured persistence store.
 */
public class HasCorrectPersistenceStoreValidator
		implements ConstraintValidator<HasCorrectPersistenceStore, PersistenceProperties> {
	
	@Override
	public boolean isValid(final PersistenceProperties persistenceProperties,
	                       final ConstraintValidatorContext constraintValidatorContext) {
		return switch (persistenceProperties.getType()) {
			case POSTGRESQL -> {
				final boolean valid = persistenceProperties.getPostgresql() != null;
				
				if (!valid) {
					constraintValidatorContext.buildConstraintViolationWithTemplate("When POSTGRESQL is used as persistence type, then the postgresql properties should be configured")
							.addConstraintViolation();
				}

				yield valid;
			}
			case SQLITE -> true;
		};
	}
}
