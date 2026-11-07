package com.beluga.persistence.sqlite.converter;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InstantConverterTest {

	private final InstantConverter converter = new InstantConverter();

	@Test
	void shouldConvertInstantToIso8601String() {
		final Instant instant = Instant.parse("2026-11-06T13:39:38Z");

		assertEquals("2026-11-06T13:39:38Z", converter.convertToDatabaseColumn(instant));
	}

	@Test
	void shouldConvertNullInstantToNull() {
		assertNull(converter.convertToDatabaseColumn(null));
	}

	@Test
	void shouldConvertIso8601StringToInstant() {
		final Instant expected = Instant.parse("2026-11-06T13:39:38Z");

		assertEquals(expected, converter.convertToEntityAttribute("2026-11-06T13:39:38Z"));
	}

	@Test
	void shouldConvertNullStringToNull() {
		assertNull(converter.convertToEntityAttribute(null));
	}

	@Test
	void shouldKeepNanosecondPrecisionWhenRoundTripping() {
		final Instant instant = Instant.parse("2026-11-06T13:39:38.901976661Z");

		final String column = converter.convertToDatabaseColumn(instant);

		assertEquals(instant, converter.convertToEntityAttribute(column));
	}

	@Test
	void shouldRejectStringThatIsNotAnInstant() {
		assertThrows(DateTimeParseException.class, () -> converter.convertToEntityAttribute("not-a-timestamp"));
	}
}
