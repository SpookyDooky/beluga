package com.beluga.properties.http.user_agent;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

/**
 * Validates {@link UserAgentProperties} with a real Bean Validation provider, covering the only-one-source rule and the
 * constraints of the rotation.
 */
class UserAgentPropertiesValidationTest {

	private static ValidatorFactory validatorFactory;
	private static Validator validator;

	@TempDir
	private Path directory;

	@BeforeAll
	static void setUp() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@AfterAll
	static void tearDown() {
		validatorFactory.close();
	}

	@Test
	void shouldBeValidWhenNothingIsConfigured() {
		final UserAgentProperties properties = Instancio.of(UserAgentProperties.class)
				.ignore(field(UserAgentProperties::getValue))
				.ignore(field(UserAgentProperties::getRotation))
				.create();

		assertThat(validator.validate(properties)).isEmpty();
	}

	@Test
	void shouldBeValidWhenOnlyValueIsConfigured() {
		final UserAgentProperties properties = Instancio.of(UserAgentProperties.class)
				.ignore(field(UserAgentProperties::getRotation))
				.create();

		assertThat(validator.validate(properties)).isEmpty();
	}

	@Test
	void shouldBeValidWhenOnlyRotationIsConfigured() throws IOException {
		final Path file = Files.writeString(directory.resolve("user-agents.txt"), "Agent/1.0\n");
		final UserAgentProperties properties = Instancio.of(UserAgentProperties.class)
				.ignore(field(UserAgentProperties::getValue))
				.set(field(UserAgentRotationProperties::getFile), file.toString())
				.create();

		assertThat(validator.validate(properties)).isEmpty();
	}

	@Test
	void shouldRejectWhenValueAndRotationAreConfigured() throws IOException {
		final Path file = Files.writeString(directory.resolve("user-agents.txt"), "Agent/1.0\n");
		final UserAgentProperties properties = Instancio.of(UserAgentProperties.class)
				.set(field(UserAgentRotationProperties::getFile), file.toString())
				.create();

		final Set<ConstraintViolation<UserAgentProperties>> violations = validator.validate(properties);

		assertThat(violations)
				.singleElement()
				.extracting(ConstraintViolation::getMessage)
				.isEqualTo("Only one of value and rotation can be configured for the user agent");
	}

	@Test
	void shouldRejectBlankRotationFile() {
		final UserAgentProperties properties = Instancio.of(UserAgentProperties.class)
				.ignore(field(UserAgentProperties::getValue))
				.set(field(UserAgentRotationProperties::getFile), " ")
				.create();

		final Set<ConstraintViolation<UserAgentProperties>> violations = validator.validate(properties);

		assertThat(violations)
				.extracting(violation -> violation.getPropertyPath().toString())
				.containsExactly("rotation.file");
	}

	@Test
	void shouldRejectMissingRotationFile() {
		final Path file = directory.resolve("missing.txt");
		final UserAgentProperties properties = Instancio.of(UserAgentProperties.class)
				.ignore(field(UserAgentProperties::getValue))
				.set(field(UserAgentRotationProperties::getFile), file.toString())
				.create();

		final Set<ConstraintViolation<UserAgentProperties>> violations = validator.validate(properties);

		assertThat(violations)
				.singleElement()
				.satisfies(violation -> {
					assertThat(violation.getPropertyPath()).hasToString("rotation.file");
					assertThat(violation.getMessage()).isEqualTo("The user agent file must exist and be readable");
				});
	}

	@Test
	void shouldRejectEmptyRotationFile() throws IOException {
		final Path file = Files.writeString(directory.resolve("user-agents.txt"), "\n  \n");
		final UserAgentProperties properties = Instancio.of(UserAgentProperties.class)
				.ignore(field(UserAgentProperties::getValue))
				.set(field(UserAgentRotationProperties::getFile), file.toString())
				.create();

		final Set<ConstraintViolation<UserAgentProperties>> violations = validator.validate(properties);

		assertThat(violations)
				.singleElement()
				.satisfies(violation -> {
					assertThat(violation.getPropertyPath()).hasToString("rotation.file");
					assertThat(violation.getMessage()).isEqualTo("The user agent file must contain at least one user agent");
				});
	}

	@Test
	void shouldRejectMissingRotationStrategy() throws IOException {
		final Path file = Files.writeString(directory.resolve("user-agents.txt"), "Agent/1.0\n");
		final UserAgentProperties properties = Instancio.of(UserAgentProperties.class)
				.ignore(field(UserAgentProperties::getValue))
				.set(field(UserAgentRotationProperties::getFile), file.toString())
				.set(field(UserAgentRotationProperties::getStrategy), null)
				.create();

		final Set<ConstraintViolation<UserAgentProperties>> violations = validator.validate(properties);

		assertThat(violations)
				.extracting(violation -> violation.getPropertyPath().toString())
				.containsExactly("rotation.strategy");
	}
}
