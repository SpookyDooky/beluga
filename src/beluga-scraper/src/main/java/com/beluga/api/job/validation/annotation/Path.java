package com.beluga.api.job.validation.annotation;

import com.beluga.api.job.validation.validator.PathValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validates whether a path starts with / and ends with a non / character.
 * Null or empty paths are considered invalid.
 */
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = PathValidator.class)
public @interface Path {
	String message() default "A path should start with a /";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
