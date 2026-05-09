package com.beluga.properties.persistence.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = HasOnlyOnePersistenceStoreValidator.class)
public @interface HasOnlyOnePersistenceStore {
	String message() default "Only one type of persistence store can be defined";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
