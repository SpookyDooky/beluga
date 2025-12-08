package com.x.scrape.api.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.scrape.api.job.dto.read.ReadJobDefinitionDto;
import com.x.scrape.api.job.dto.write.WriteJobDefinitionDto;
import com.x.scrape.api.task.dto.UpdateTaskDto;
import com.x.scrape.integration_test.BaseIntegrationTest;
import org.instancio.Instancio;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class TaskControllerIntegrationTest extends BaseIntegrationTest {
	
	@Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper objectMapper;
	
	@TestTemplate
	void shouldGetTasks() throws Exception {
		final WriteJobDefinitionDto writeJobDefinitionDto = Instancio.create(WriteJobDefinitionDto.class);
		
		Thread.sleep(250);
		
		final ReadJobDefinitionDto jobDefinition = createJob(writeJobDefinitionDto);
		
		final UpdateTaskDto updateTaskDto = Instancio.create(UpdateTaskDto.class);
		
		mvc.perform(put("/jobs/" + jobDefinition.getId() + "/tasks")
				.content(objectMapper.writeValueAsString(updateTaskDto))
				.contentType(APPLICATION_JSON)
		).andExpect(status().isOk());
		
		mvc.perform(get("/jobs/" + jobDefinition.getId() + "/tasks"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(updateTaskDto.getUrls().size()));
	}
	
	@TestTemplate
	void shouldGetTasks404() throws Exception {
		mvc.perform(get("/jobs/123/tasks"))
				.andExpect(status().isNotFound());
	}
	
	@TestTemplate
	void shouldGet404ForRetrievingNonExistentTask() throws Exception {
		final WriteJobDefinitionDto writeJobDefinitionDto = Instancio.create(WriteJobDefinitionDto.class);
		Thread.sleep(250);
		final ReadJobDefinitionDto jobDefinition = createJob(writeJobDefinitionDto);
		
		mvc.perform(get("/jobs/" + jobDefinition.getId() + "/tasks/321"))
				.andExpect(status().isNotFound());
	}
	
	@TestTemplate
	void shouldUpdateTasks() throws Exception {
		final WriteJobDefinitionDto writeJobDefinitionDto = Instancio.create(WriteJobDefinitionDto.class);
		
		Thread.sleep(250);
		
		final ReadJobDefinitionDto jobDefinition = createJob(writeJobDefinitionDto);
		
		final UpdateTaskDto updateTaskDto = Instancio.create(UpdateTaskDto.class);
		
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
	
	@TestTemplate
	void shouldReturn404WhenUpdatingTasksForNonExistingJob() throws Exception {
		final UpdateTaskDto updateTaskDto = Instancio.create(UpdateTaskDto.class);
		
		mvc.perform(put("/jobs/123/tasks")
				.content(objectMapper.writeValueAsString(updateTaskDto))
				.contentType(APPLICATION_JSON)
		).andExpect(status().isNotFound());
	}
}
