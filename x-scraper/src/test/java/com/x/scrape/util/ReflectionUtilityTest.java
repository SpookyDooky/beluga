package com.x.scrape.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;

import static com.x.scrape.util.ReflectionUtility.getFieldValue;
import static com.x.scrape.util.ReflectionUtility.isValueHolder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReflectionUtilityTest {
	
	@ParameterizedTest
	@MethodSource
	void shouldBeValueHolder(final Object object) {
		assertTrue(isValueHolder(object));
	}
	
	static Stream<Arguments> shouldBeValueHolder() {
		return Stream.of(
				(byte) 1,
				Byte.valueOf((byte) 1),
				(short) 1,
				Short.valueOf((short) 1),
				1,
				Integer.valueOf(1),
				(long) 1,
				Long.valueOf(1),
				1.0,
				Double.valueOf(1.0),
				(float) 1.0,
				Float.valueOf((float) 1.0),
				UUID.randomUUID(),
				"string",
				Instant.now()
		).map(Arguments::of);
	}
	
	@Test
	void shouldGetFieldValue() throws Exception {
		final TestClass testClass = new TestClass();
		
		assertEquals("value", getFieldValue(testClass, testClass.getClass().getDeclaredField("value")));
	}
	
	static class TestClass {
		private String value = "value";
	}
}