package com.beluga.api.job.validation.validator;

import com.beluga.api.job.validation.annotation.Path;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PathValidator implements ConstraintValidator<Path, String> {
	
	@Override
	public boolean isValid(final String path,
	                       final ConstraintValidatorContext constraintValidatorContext) {
		if (path == null) {
			return false;
		}
		
		if (!path.startsWith("/") || path.endsWith("/")) {
			constraintValidatorContext.buildConstraintViolationWithTemplate("A path should begin with / and end with a non / character")
					.addConstraintViolation();
			return false;
		}
		
		return true;
	}
}
