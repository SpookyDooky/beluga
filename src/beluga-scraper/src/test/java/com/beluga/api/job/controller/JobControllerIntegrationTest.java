package com.beluga.api.job.controller;

import com.beluga.api.job.dto.base.BaseJobDefinitionDto;
import com.beluga.api.job.dto.read.ReadJobDefinitionDto;
import com.beluga.api.job.dto.write.WriteJobDefinitionDto;
import com.beluga.integration_test.BaseIntegrationTest;
import org.instancio.Instancio;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.hasSize;
import static org.instancio.Select.field;
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
		final long jobId = 99999999L;
		
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

	@TestTemplate
	void shouldReturnErrorResponseForInvalidJob() throws Exception {
		final WriteJobDefinitionDto jobDefinition = Instancio.of(WriteJobDefinitionDto.class)
				.set(field(BaseJobDefinitionDto::getName), "")
				.create();
		
		// Account for async application configuration
		Thread.sleep(250);
		
		mvc.perform(post("/jobs")
						.content(objectMapper.writeValueAsString(jobDefinition))
						.contentType(APPLICATION_JSON)
				).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is(400)))
				.andExpect(jsonPath("$.timestamp", isA(String.class)))
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors[0]", not(emptyString())));
	}
	
	@TestTemplate
	void shouldReturn400ForDuplicateJobName() throws Exception {
		final WriteJobDefinitionDto jobDefinition = Instancio.create(WriteJobDefinitionDto.class);
		
		// Account for async application configuration
		Thread.sleep(250);
		
		mvc.perform(post("/jobs")
				.content(objectMapper.writeValueAsString(jobDefinition))
				.contentType(APPLICATION_JSON)
		).andExpect(status().isOk());
		
		mvc.perform(post("/jobs")
						.content(objectMapper.writeValueAsString(jobDefinition))
						.contentType(APPLICATION_JSON)
				).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasItem("Job name must be unique.")));
	}
}
