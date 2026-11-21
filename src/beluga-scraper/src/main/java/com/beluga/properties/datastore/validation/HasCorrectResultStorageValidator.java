package com.beluga.properties.datastore.validation;

import com.beluga.properties.datastore.DataStoreType;
import com.beluga.properties.datastore.ResultStorageProperties;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates if the configured {@link DataStoreType} is compatible with the actual
 * configured result storage.
 */
public class HasCorrectResultStorageValidator
		implements ConstraintValidator<HasCorrectResultStorage, ResultStorageProperties> {

	@Override
	public boolean isValid(final ResultStorageProperties resultStorageProperties,
	                       final ConstraintValidatorContext constraintValidatorContext) {
		return switch (resultStorageProperties.getType()) {
			case S3 -> {
				final boolean valid = resultStorageProperties.getS3() != null;

				if (!valid) {
					constraintValidatorContext.buildConstraintViolationWithTemplate("When S3 is used as result storage type, then the s3 properties should be configured")
							.addConstraintViolation();
				}

				yield valid;
			}
			case FILE_SYSTEM -> true;
		};
	}
}
