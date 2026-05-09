package com.x.scrape.result_storage.json.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class ObjectMapperConfig {
	
	/**
	 * Creates an {@link ObjectMapper} bean with all the correct settings and/or flags enabled.
	 */
	@Bean
	public ObjectMapper objectMapper() {
		return JsonMapper.builder()
				.findAndAddModules()
				.build();
	}
}
