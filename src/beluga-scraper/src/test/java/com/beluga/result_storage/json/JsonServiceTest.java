package com.beluga.result_storage.json;

import com.beluga.result_storage.json.config.ObjectMapperConfig;
import org.junit.jupiter.api.Test;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Uses the production {@link ObjectMapper} and {@link JsonNestingService}, as the JSON they produce together is what
 * gets stored as the result of a task.
 */
class JsonServiceTest {

	private final ObjectMapper objectMapper = new ObjectMapperConfig().objectMapper();
	private final JsonService jsonService = new JsonService(objectMapper, new JsonNestingService());

	@Test
	void shouldNestDottedKeysOfMapsInCollection() {
		final Object content = List.of(
				Map.of("product.price.whole", "10", "product.price.fractional", "99", "name", "first"),
				Map.of("product.price.whole", "20", "name", "second")
		);

		assertJsonEquals(
				"""
				[
				  {"product": {"price": {"whole": "10", "fractional": "99"}}, "name": "first"},
				  {"product": {"price": {"whole": "20"}}, "name": "second"}
				]
				""",
				jsonService.toJson(content)
		);
	}

	@Test
	void shouldNestMapsInCollectionThatIsNotAList() {
		final Object content = Set.of(Map.of("a.b", "c"));

		assertJsonEquals("[{\"a\": {\"b\": \"c\"}}]", jsonService.toJson(content));
	}

	@Test
	void shouldWriteNullValuesInCollection() {
		final Map<String, Object> item = new HashMap<>();
		item.put("missing", null);
		item.put("nested.missing", null);
		final Object content = List.of(item);

		assertJsonEquals("[{\"missing\": null, \"nested\": {\"missing\": null}}]", jsonService.toJson(content));
	}

	@Test
	void shouldWriteEmptyCollection() {
		final Object content = List.of();

		assertEquals("[]", jsonService.toJson(content));
	}

	@Test
	void shouldWriteCollectionItemsThatAreNotMapsUnchanged() {
		final Object content = List.of("text", 1, List.of("nested.value"));

		assertJsonEquals("[\"text\", 1, [\"nested.value\"]]", jsonService.toJson(content));
	}

	@Test
	void shouldNestMapsInCollectionWithOtherItemTypes() {
		final Object content = List.of("text", Map.of("a.b", "c"));

		assertJsonEquals("[\"text\", {\"a\": {\"b\": \"c\"}}]", jsonService.toJson(content));
	}

	@Test
	void shouldWriteMapsWithNonStringKeysUnchanged() {
		final Object content = List.of(Map.of(1, "value"));

		assertJsonEquals("[{\"1\": \"value\"}]", jsonService.toJson(content));
	}

	@Test
	void shouldNotNestKeysOfMapOutsideCollection() {
		final Object content = Map.of("a.b", "c");

		assertJsonEquals("{\"a.b\": \"c\"}", jsonService.toJson(content));
	}

	@Test
	void shouldWriteObjectThatIsNotCollection() {
		final Object content = "text";

		assertEquals("\"text\"", jsonService.toJson(content));
	}

	@Test
	void shouldWriteNullObject() {
		final Object content = null;

		assertEquals("null", jsonService.toJson(content));
	}

	@Test
	void shouldThrowWhenObjectCanNotBeSerialized() {
		final Object content = new NotSerializable();

		assertSerializationFailure(() -> jsonService.toJson(content));
	}

	@Test
	void shouldThrowWhenCollectionItemCanNotBeSerialized() {
		final Object content = Arrays.asList(new NotSerializable());

		assertSerializationFailure(() -> jsonService.toJson(content));
	}

	@Test
	void shouldNestDottedKeysOfListOfMaps() {
		final List<Map<String, Object>> content = List.of(
				Map.of("a.b", "c"),
				Map.of("d", "e")
		);

		assertJsonEquals("[{\"a\": {\"b\": \"c\"}}, {\"d\": \"e\"}]", jsonService.toJson(content));
	}

	@Test
	void shouldWriteEmptyListOfMaps() {
		final List<Map<String, Object>> content = List.of();

		assertEquals("[]", jsonService.toJson(content));
	}

	@Test
	void shouldThrowWhenListOfMapsCanNotBeSerialized() {
		final List<Map<String, Object>> content = List.of(Map.of("field", new NotSerializable()));

		assertSerializationFailure(() -> jsonService.toJson(content));
	}

	private void assertJsonEquals(final String expected,
	                              final String actual) {
		assertEquals(objectMapper.readTree(expected), objectMapper.readTree(actual));
	}

	private void assertSerializationFailure(final Runnable serialization) {
		final IllegalStateException exception = assertThrows(IllegalStateException.class, serialization::run);

		assertEquals("Failed to convert object to JSON.", exception.getMessage());
		assertInstanceOf(JacksonException.class, exception.getCause());
	}

	/**
	 * Jackson fails on this class as reading its only property throws an exception.
	 */
	public static class NotSerializable {

		public String getValue() {
			throw new UnsupportedOperationException("Not readable");
		}
	}
}
