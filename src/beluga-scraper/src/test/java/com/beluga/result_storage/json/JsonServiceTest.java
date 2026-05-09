package com.beluga.result_storage.json;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JsonServiceTest {
	
	@Mock
	private ObjectMapper objectMapper;
	@Mock
	private JsonNestingService jsonNestingService;
	
	@InjectMocks
	private JsonService jsonService;
	
	@Test
	void shouldCreateJson() throws Exception {
		final List<Map<String, Object>> content = List.of(mock(Map.class));
		when(jsonNestingService.createJsonNesting(content.getFirst()))
				.thenReturn(content.getFirst());
		
		final String json = "json";
		when(objectMapper.writeValueAsString(content)).thenReturn(json);
		
		final String result = jsonService.toJson(content);
		
		assertSame(json, result);
	}
}