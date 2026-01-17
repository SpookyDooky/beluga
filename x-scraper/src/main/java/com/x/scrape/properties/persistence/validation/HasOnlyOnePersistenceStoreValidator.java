package com.x.scrape.properties.persistence.validation;

import com.x.scrape.properties.persistence.PersistenceProperties;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HasOnlyOnePersistenceStoreValidator
		implements ConstraintValidator<HasOnlyOnePersistenceStore, PersistenceProperties> {
	
	@Override
	public boolean isValid(final PersistenceProperties persistenceProperties,
	                       final ConstraintValidatorContext constraintValidatorContext) {
		int count = 0;
		
		count += persistenceProperties.getPostgresql() != null ? 1 : 0;
		
		return count == 1;
	}
}
