package com.x.scrape.result_storage.json.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

@Configuration
public class ObjectMapperConfig {
	
	/**
	 * Creates an {@link ObjectMapper} bean with all the correct settings and/or flags enabled.
	 */
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper()
				.findAndRegisterModules()
				.enable(INDENT_OUTPUT);
	}
}
