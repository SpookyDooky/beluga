package com.x.scrape.api.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.scrape.api.job.dto.read.ReadJobDefinitionDto;
import com.x.scrape.api.job.dto.write.WriteJobDefinitionDto;
import com.x.scrape.api.task.dto.UpdateTaskDto;
import com.x.scrape.integration_test.BaseIntegrationTest;
import org.instancio.Instancio;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URL;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class TaskControllerPerformanceIntegrationTest extends BaseIntegrationTest {
	
	@Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper objectMapper;
	
	@TestTemplate
	@Timeout(10_000)
	void shouldUpdateTasks() throws Exception {
		final WriteJobDefinitionDto writeJobDefinitionDto = Instancio.create(WriteJobDefinitionDto.class);
		Thread.sleep(250);
		final ReadJobDefinitionDto jobDefinition = createJob(writeJobDefinitionDto);
		
		final UpdateTaskDto updateTaskDto = new UpdateTaskDto();
		
		for (int i = 0; i < 10_000; i++) {
			updateTaskDto.getUrls().add(createUrl());
		}
		
		mvc.perform(put("/jobs/" + jobDefinition.getId() + "/tasks")
				.content(objectMapper.writeValueAsString(updateTaskDto))
				.contentType(APPLICATION_JSON)
		).andExpect(status().isOk());
	}
	
	ReadJobDefinitionDto createJob(final WriteJobDefinitionDto writeJobDefinitionDto) throws Exception {
		final MvcResult result = mvc.perform(post("/jobs")
				.content(objectMapper.writeValueAsString(writeJobDefinitionDto))
				.contentType(APPLICATION_JSON)
		).andReturn();
		
		final byte[] expectedContent = result.getResponse().getContentAsByteArray();
		return objectMapper.readValue(expectedContent, ReadJobDefinitionDto.class);
	}
	
	URL createUrl() throws Exception {
		return new URL("http://localhost:1234/" + UUID.randomUUID());
	}
}
