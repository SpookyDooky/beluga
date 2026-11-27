package com.beluga.properties.http.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = ValidMaxBodySizeValidator.class)
public @interface ValidMaxBodySize {
	String message() default "The max body size must be at least 1 byte and smaller than 2GB";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
