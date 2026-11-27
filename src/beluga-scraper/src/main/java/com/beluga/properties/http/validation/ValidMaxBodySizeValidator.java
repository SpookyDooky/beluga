package com.beluga.properties.http.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.unit.DataSize;

/**
 * Validates that the max body size fits the int that jsoup accepts. The largest allowed value is one byte below
 * {@link Integer#MAX_VALUE}, because the http service asks jsoup for one byte more than the maximum to detect bodies
 * that are too large. Null values are considered valid.
 */
public class ValidMaxBodySizeValidator implements ConstraintValidator<ValidMaxBodySize, DataSize> {

	private static final long MIN_BYTES = 1;
	private static final long MAX_BYTES = Integer.MAX_VALUE - 1L;

	@Override
	public boolean isValid(final DataSize maxBodySize,
	                       final ConstraintValidatorContext constraintValidatorContext) {
		if (maxBodySize == null) {
			return true;
		}

		final long bytes = maxBodySize.toBytes();
		return bytes >= MIN_BYTES && bytes <= MAX_BYTES;
	}
}
