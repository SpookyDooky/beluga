package com.beluga.properties.http.user_agent.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Validates that the user agent file exists and is a readable regular file that contains at least one user agent. Blank
 * lines are not user agents, therefore a file with only blank lines is invalid. Null and blank values are considered valid.
 * A file that can't be read as UTF-8 throws an
 * {@link UncheckedIOException}.
 */
public class ValidUserAgentFileValidator implements ConstraintValidator<ValidUserAgentFile, String> {

	private static final String UNREADABLE_FILE_MESSAGE = "The user agent file must exist and be readable";
	private static final String EMPTY_FILE_MESSAGE = "The user agent file must contain at least one user agent";

	@Override
	public boolean isValid(final String file,
	                       final ConstraintValidatorContext constraintValidatorContext) {
		if (file == null || file.isBlank()) {
			return true;
		}

		final Path path;
		try {
			path = Path.of(file);
		} catch (final InvalidPathException e) {
			return invalid(constraintValidatorContext, UNREADABLE_FILE_MESSAGE);
		}

		if (!Files.isRegularFile(path) || !Files.isReadable(path)) {
			return invalid(constraintValidatorContext, UNREADABLE_FILE_MESSAGE);
		}

		if (!containsUserAgent(path)) {
			return invalid(constraintValidatorContext, EMPTY_FILE_MESSAGE);
		}

		return true;
	}

	private boolean containsUserAgent(final Path path) {
		try (final Stream<String> lines = Files.lines(path)) {
			return lines.anyMatch(line -> !line.isBlank());
		} catch (final IOException e) {
			throw new IllegalStateException("Failed to read the user agent file " + path + ".", e);
		}
	}

	private boolean invalid(final ConstraintValidatorContext constraintValidatorContext,
	                        final String message) {
		constraintValidatorContext.disableDefaultConstraintViolation();
		constraintValidatorContext.buildConstraintViolationWithTemplate(message)
				.addConstraintViolation();

		return false;
	}
}
