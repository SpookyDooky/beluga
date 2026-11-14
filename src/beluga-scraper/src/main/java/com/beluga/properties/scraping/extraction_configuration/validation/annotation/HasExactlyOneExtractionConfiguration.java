package com.beluga.properties.scraping.extraction_configuration.validation.annotation;

import com.beluga.properties.scraping.extraction_configuration.validation.validator.HasExactlyOneExtractionConfigurationValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validates that a data point configures exactly one extraction type.
 */
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = HasExactlyOneExtractionConfigurationValidator.class)
public @interface HasExactlyOneExtractionConfiguration {
    String message() default "Exactly one extraction type must be configured";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
