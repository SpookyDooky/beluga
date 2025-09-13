package com.x.scrape.storage.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JsonService {

	private final ObjectMapper objectMapper;
	private final JsonNestingService jsonNestingService;
	
	public JsonService(final ObjectMapper objectMapper,
	                   final JsonNestingService jsonNestingService) {
		this.objectMapper = objectMapper;
		this.jsonNestingService = jsonNestingService;
	}
	
	public String toJson(final List<Map<String, Object>> content) {
		final List<Map<String, Object>> nestedContent = content.stream()
				.map(jsonNestingService::createJsonNesting)
				.toList();
		
		try {
			return objectMapper.writeValueAsString(nestedContent);
		} catch (final JsonProcessingException e) {
			throw new IllegalStateException("Failed to convert object to JSON.", e);
		}
	}
}
