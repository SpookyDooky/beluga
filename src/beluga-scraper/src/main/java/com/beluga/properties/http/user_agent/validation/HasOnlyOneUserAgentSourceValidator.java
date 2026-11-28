package com.beluga.properties.http.user_agent.validation;

import com.beluga.properties.http.user_agent.UserAgentProperties;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates that a user agent is either a fixed value or a rotation, not both. Configuring neither is valid, the
 * default user agent is used then.
 */
public class HasOnlyOneUserAgentSourceValidator
		implements ConstraintValidator<HasOnlyOneUserAgentSource, UserAgentProperties> {

	@Override
	public boolean isValid(final UserAgentProperties userAgentProperties,
	                       final ConstraintValidatorContext constraintValidatorContext) {
		return userAgentProperties.getValue() == null || userAgentProperties.getRotation() == null;
	}
}
