package com.x.scrape.api.job.controller;

import com.x.scrape.api.job.dto.read.ReadJobDefinitionDto;
import com.x.scrape.api.job.dto.write.WriteJobDefinitionDto;
import com.beluga.integration_test.BaseIntegrationTest;
import org.instancio.Instancio;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class JobControllerIntegrationTest extends BaseIntegrationTest {
	
	@Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper objectMapper;
	
	@TestTemplate
	void shouldCreateJob() throws Exception {
		final WriteJobDefinitionDto jobDefinition = Instancio.create(WriteJobDefinitionDto.class);
		
		// Account for async application configuration
		Thread.sleep(250);
		
		mvc.perform(post("/jobs")
						.content(objectMapper.writeValueAsString(jobDefinition))
						.contentType(APPLICATION_JSON)
				).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", notNullValue()));
	}
	
	@TestTemplate
	void shouldGetJob() throws Exception {
		final WriteJobDefinitionDto jobDefinition = Instancio.create(WriteJobDefinitionDto.class);
		
		// Account for async application configuration
		Thread.sleep(250);
		
		final MvcResult result = mvc.perform(post("/jobs")
				.content(objectMapper.writeValueAsString(jobDefinition))
				.contentType(APPLICATION_JSON)
		).andReturn();
		
		final byte[] expectedContent = result.getResponse().getContentAsByteArray();
		final ReadJobDefinitionDto contentDto = objectMapper.readValue(expectedContent, ReadJobDefinitionDto.class);
		
		final byte[] actualContent = mvc.perform(get("/jobs/" + contentDto.getId()))
				.andReturn().getResponse().getContentAsByteArray();
		
		assertArrayEquals(expectedContent, actualContent);
	}
	
	@TestTemplate
	void shouldGetJobReturn404() throws Exception {
		final Long jobId = 99999999L;
		
		mvc.perform(get("/jobs/" + jobId))
				.andExpect(status().isNotFound());
	}
	
	@TestTemplate
	void shouldUpdateJob() throws Exception {
		final WriteJobDefinitionDto jobDefinition = Instancio.create(WriteJobDefinitionDto.class);
		
		// Account for async application configuration
		Thread.sleep(250);
		
		final MvcResult result = mvc.perform(post("/jobs")
				.content(objectMapper.writeValueAsString(jobDefinition))
				.contentType(APPLICATION_JSON)
		).andReturn();
		
		final byte[] expectedContent = result.getResponse().getContentAsByteArray();
		final ReadJobDefinitionDto contentDto = objectMapper.readValue(expectedContent, ReadJobDefinitionDto.class);
		
		mvc.perform(put("/jobs/" + contentDto.getId())
				.content(objectMapper.writeValueAsString(jobDefinition))
				.contentType(APPLICATION_JSON)
		).andExpect(status().isOk());
	}
}
