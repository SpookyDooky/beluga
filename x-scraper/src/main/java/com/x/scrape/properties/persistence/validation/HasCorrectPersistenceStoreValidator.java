package com.x.scrape.properties.persistence.validation;

import com.x.scrape.properties.persistence.PersistenceProperties;
import com.x.scrape.properties.persistence.PersistenceType;
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
			case S3 -> {
				final boolean valid = persistenceProperties.getS3() != null;
				
				if (!valid) {
					constraintValidatorContext.buildConstraintViolationWithTemplate("When S3 is used as persistence type, then the s3 properties should be configured")
							.addConstraintViolation();
				}
				
				yield valid;
			}
			case FILE_SYSTEM -> {
				System.out.println("Am i being ran");
				final boolean valid = persistenceProperties.getFileSystem() != null;
				
				if (!valid) {
					constraintValidatorContext.buildConstraintViolationWithTemplate("When FILE_SYSTEM is used as persistence type, then the file-system properties should be configured")
							.addConstraintViolation();
				}
				yield valid;
			}
			case POSTGRESQL -> {
				constraintValidatorContext.buildConstraintViolationWithTemplate("PostgreSQL is currently not supported as a persistence store")
						.addConstraintViolation();
				yield false;
			}
		};
	}
}
