package com.x.scrape.properties.persistence.validation;

import com.x.scrape.properties.persistence.PersistenceProperties;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HasOnlyOnePersistenceStoreValidator
		implements ConstraintValidator<HasOnlyOnePersistenceStore, PersistenceProperties> {
	
	@Override
	public boolean isValid(final PersistenceProperties persistenceProperties,
	                       final ConstraintValidatorContext constraintValidatorContext) {
		return persistenceProperties.getFileSystem() != null ^
				persistenceProperties.getS3() != null;
	}
}
