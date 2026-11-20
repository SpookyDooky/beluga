package com.beluga.api.job.validation.annotation;

import com.beluga.api.job.validation.validator.ValidFieldNamesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validates whether field names for data storage do not class or overlap.
 */
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = ValidFieldNamesValidator.class)
public @interface ValidFieldNames {
    String message() default "Field names must be unique, well-formed and must not conflict with nested field names";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
