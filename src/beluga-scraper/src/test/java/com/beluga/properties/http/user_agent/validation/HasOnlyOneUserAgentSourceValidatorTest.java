package com.beluga.properties.http.user_agent.validation;

import com.beluga.properties.http.user_agent.UserAgentProperties;
import jakarta.validation.ConstraintValidatorContext;
import org.instancio.Instancio;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class HasOnlyOneUserAgentSourceValidatorTest {

	@Mock
	private ConstraintValidatorContext context;

	private final HasOnlyOneUserAgentSourceValidator validator = new HasOnlyOneUserAgentSourceValidator();

	@ParameterizedTest
	@MethodSource
	void shouldBeValid(final UserAgentProperties userAgentProperties) {
		assertTrue(validator.isValid(userAgentProperties, context));
	}

	static Stream<Arguments> shouldBeValid() {
		return Stream.of(
				Instancio.of(UserAgentProperties.class)
						.ignore(field(UserAgentProperties::getValue))
						.ignore(field(UserAgentProperties::getRotation))
						.create(),
				Instancio.of(UserAgentProperties.class)
						.ignore(field(UserAgentProperties::getRotation))
						.create(),
				Instancio.of(UserAgentProperties.class)
						.ignore(field(UserAgentProperties::getValue))
						.create()
		).map(Arguments::of);
	}

	@ParameterizedTest
	@MethodSource
	void shouldBeInvalid(final UserAgentProperties userAgentProperties) {
		assertFalse(validator.isValid(userAgentProperties, context));
	}

	static Stream<Arguments> shouldBeInvalid() {
		return Stream.of(
				Instancio.create(UserAgentProperties.class)
		).map(Arguments::of);
	}
}
