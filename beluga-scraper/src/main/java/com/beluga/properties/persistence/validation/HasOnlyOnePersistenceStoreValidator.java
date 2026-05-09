package com.beluga.properties.persistence.validation;

import com.beluga.properties.persistence.PersistenceProperties;
import com.beluga.properties.persistence.PersistenceType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HasOnlyOnePersistenceStoreValidator
		implements ConstraintValidator<HasOnlyOnePersistenceStore, PersistenceProperties> {
	
	@Override
	public boolean isValid(final PersistenceProperties persistenceProperties,
	                       final ConstraintValidatorContext constraintValidatorContext) {
		int count = 0;
		
		count += persistenceProperties.getPostgresql() != null ? 1 : 0;
		
		if (persistenceProperties.getType() == PersistenceType.SQL_LITE) {
			return count == 0;
		}
		
		return count == 1;
	}
}
