package com.beluga.properties.http.user_agent.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = HasOnlyOneUserAgentSourceValidator.class)
public @interface HasOnlyOneUserAgentSource {
	String message() default "Only one of value and rotation can be configured for the user agent";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
