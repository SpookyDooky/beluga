package com.beluga.api.job.validation.annotation;

import com.beluga.api.job.validation.validator.JobNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = JobNameValidator.class)
public @interface JobName {
    String message() default "Job name not unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
