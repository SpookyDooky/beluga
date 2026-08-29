package com.beluga.properties.scraping.extraction_configuration.validation.annotation;

import com.beluga.properties.scraping.extraction_configuration.validation.validator.ExtractionConfigurationPropertiesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = ExtractionConfigurationPropertiesValidator.class)
public @interface ExtractionConfiguration {
    String message() default "Invalid extraction configuration";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
