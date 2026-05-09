package com.x.scrape.properties.persistence.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = HasCorrectPersistenceStoreValidator.class)
public @interface HasCorrectPersistenceStore {
	String message() default "Incompatible persistence store configured with persistence type";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
