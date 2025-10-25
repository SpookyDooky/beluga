package com.x.scrape.result_storage.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
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
	
	public String toJson(final Object object) {
		if (isObjectCollection(object)) {
			final Collection<Object> objects = (Collection<Object>) object;
			return toRawJsonString(processCollection(objects));
		}
		
		return toRawJsonString(object);
	}
	
	private Object processCollection(final Collection<Object> objects) {
		return objects.stream()
				.map(this::mapCollectionObject)
				.toList();
	}
	
	private Object mapCollectionObject(final Object object) {
		try {
			final Map<String, Object> objectMap = (Map<String, Object>) object;
			return jsonNestingService.createJsonNesting(objectMap);
		} catch (final ClassCastException e) {
			return object;
		}
	}
	
	private String toRawJsonString(final Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (final JsonProcessingException e) {
			throw new IllegalStateException("Failed to convert object to JSON.", e);
		}
	}
	
	private boolean isObjectCollection(final Object object) {
		return object instanceof Collection;
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
