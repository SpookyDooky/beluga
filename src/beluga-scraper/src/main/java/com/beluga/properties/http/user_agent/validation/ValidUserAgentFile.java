package com.beluga.properties.http.user_agent.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = ValidUserAgentFileValidator.class)
public @interface ValidUserAgentFile {
	String message() default "Invalid user agent file";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
