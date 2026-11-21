package com.beluga.properties.datastore.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = HasCorrectResultStorageValidator.class)
public @interface HasCorrectResultStorage {
	String message() default "Incompatible result storage configured with result storage type";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
