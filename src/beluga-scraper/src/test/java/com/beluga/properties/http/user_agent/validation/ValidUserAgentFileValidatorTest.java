package com.beluga.properties.http.user_agent.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ValidUserAgentFileValidatorTest {

	private static final String UNREADABLE_FILE_MESSAGE = "The user agent file must exist and be readable";
	private static final String EMPTY_FILE_MESSAGE = "The user agent file must contain at least one user agent";

	@TempDir
	private Path directory;

	@Mock(answer = RETURNS_DEEP_STUBS)
	private ConstraintValidatorContext context;

	private final ValidUserAgentFileValidator validator = new ValidUserAgentFileValidator();

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {
			" "
	})
	void shouldBeValidForNullOrBlankFile(final String file) {
		assertTrue(validator.isValid(file, context));
	}

	@Test
	void shouldBeValidForFileWithUserAgent() throws IOException {
		final Path file = Files.writeString(directory.resolve("user-agents.txt"), "Agent/1.0\n");

		assertTrue(validator.isValid(file.toString(), context));
	}

	@Test
	void shouldBeValidForFileWithUserAgentBetweenBlankLines() throws IOException {
		final Path file = Files.writeString(directory.resolve("user-agents.txt"), "\n  \nAgent/1.0\n\t\n");

		assertTrue(validator.isValid(file.toString(), context));
	}

	@Test
	void shouldBeInvalidForMissingFile() {
		final Path file = directory.resolve("missing.txt");

		assertFalse(validator.isValid(file.toString(), context));
		verify(context).buildConstraintViolationWithTemplate(UNREADABLE_FILE_MESSAGE);
	}

	@Test
	void shouldBeInvalidForDirectory() {
		assertFalse(validator.isValid(directory.toString(), context));
		verify(context).buildConstraintViolationWithTemplate(UNREADABLE_FILE_MESSAGE);
	}

	@Test
	void shouldBeInvalidForInvalidPath() {
		assertFalse(validator.isValid("user\0agents.txt", context));
		verify(context).buildConstraintViolationWithTemplate(UNREADABLE_FILE_MESSAGE);
	}

	@Test
	void shouldBeInvalidForEmptyFile() throws IOException {
		final Path file = Files.createFile(directory.resolve("user-agents.txt"));

		assertFalse(validator.isValid(file.toString(), context));
		verify(context).buildConstraintViolationWithTemplate(EMPTY_FILE_MESSAGE);
	}

	@Test
	void shouldBeInvalidForFileWithOnlyBlankLines() throws IOException {
		final Path file = Files.writeString(directory.resolve("user-agents.txt"), "\n  \n\t\r\n");

		assertFalse(validator.isValid(file.toString(), context));
		verify(context).buildConstraintViolationWithTemplate(EMPTY_FILE_MESSAGE);
	}

	@Test
	void shouldThrowForFileThatIsNotUtf8() throws IOException {
		final Path file = Files.write(
				directory.resolve("user-agents.txt"),
				new byte[]{
						(byte) 0xC3,
						(byte) 0x28
				}
		);

		assertThatThrownBy(() -> validator.isValid(file.toString(), context))
				.isInstanceOf(UncheckedIOException.class)
				.hasMessageContaining(file.toString())
				.hasCauseInstanceOf(MalformedInputException.class);
	}
}
