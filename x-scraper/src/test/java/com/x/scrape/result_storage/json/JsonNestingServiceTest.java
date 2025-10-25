package com.x.scrape.result_storage.json;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonNestingServiceTest {
	
	private final JsonNestingService jsonNestingService = new JsonNestingService();
	
	@Test
	void shouldNestObjects() {
		final Map<String, Object> mapWithNesting = Map.of(
				"property.test1", "a",
				"property.test2", "b",
				"thisIsFine", "c",
				"product.price.whole", "d",
				"product.price.fractional", "e",
				"product.origin.country.code", "f",
				"product.origin.country.name", "g"
		);
		
		final Map<String, Object> result = jsonNestingService.createJsonNesting(mapWithNesting);
		assertEquals("c", result.get("thisIsFine"));
		
		final Map<String, Object> propertyMap = (Map<String, Object>) result.get("property");
		assertEquals("a", propertyMap.get("test1"));
		assertEquals("b", propertyMap.get("test2"));
		
		final Map<String, Object> productMap = (Map<String, Object>) result.get("product");
		final Map<String, Object> priceMap = (Map<String, Object>) productMap.get("price");
		assertEquals("d", priceMap.get("whole"));
		assertEquals("e", priceMap.get("fractional"));
		
		final Map<String, Object> originMap = (Map<String, Object>) productMap.get("origin");
		final Map<String, Object> countryMap = (Map<String, Object>) originMap.get("country");
		assertEquals("f", countryMap.get("code"));
		assertEquals("g", countryMap.get("name"));
	}
}