package com.x.scrape.api.results.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.scrape.api.execution.dto.ReadJobExecutionDto;
import com.x.scrape.api.execution.dto.ReadJobExecutionWithTasksDto;
import com.x.scrape.api.job.dto.read.ReadJobDefinitionDto;
import com.x.scrape.api.job.dto.write.WriteExecutionConfigurationDto;
import com.x.scrape.api.job.dto.write.WriteJobDefinitionDto;
import com.x.scrape.api.task.dto.UpdateTaskDto;
import com.x.scrape.integration_test.MultiStoreTest;
import com.x.scrape.result_storage.StorageService;
import com.x.scrape.scraping.ScrapingService;
import org.instancio.Instancio;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

import static org.instancio.Select.field;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@MultiStoreTest
@AutoConfigureMockMvc
class ResultsControllerIntegrationTest {
	
	@MockitoBean
	private StorageService storageService;
	@MockitoBean(answers = RETURNS_DEEP_STUBS)
	private ScrapingService scrapingService;
	
	@Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper objectMapper;
	
	@TestTemplate
	void shouldGetTaskResult() throws Exception {
		final ReadJobDefinitionDto readJobDefinitionDto = createJob(
				Instancio.of(WriteJobDefinitionDto.class)
						.set(
								field(WriteJobDefinitionDto::getExecution),
								Instancio.of(WriteExecutionConfigurationDto.class)
										.set(field(WriteExecutionConfigurationDto::getWorkers), 1)
										.set(field(WriteExecutionConfigurationDto::getTasksPerSecond), 10.0)
										.create()
						).create()
		);
		
		// Add tasks to job
		final UpdateTaskDto updateTaskDto = Instancio.create(UpdateTaskDto.class);
		mvc.perform(put("/jobs/" + readJobDefinitionDto.getId() + "/tasks")
				.content(objectMapper.writeValueAsString(updateTaskDto))
				.contentType(APPLICATION_JSON)
		).andExpect(status().isOk());
		
		// Start job
		mvc.perform(post("/jobs/" + readJobDefinitionDto.getId() + "/start"))
				.andExpect(status().isNoContent());
		
		Thread.sleep(1000);
		// Retrieve latest execution
		final MvcResult latestExecutionResult = mvc.perform(
				get("/jobs/" + readJobDefinitionDto.getId() + "/executions/latest")
		).andReturn();
		
		final ReadJobExecutionDto readJobExecutionDto = objectMapper.readValue(
				latestExecutionResult.getResponse().getContentAsByteArray(),
				ReadJobExecutionDto.class
		);
		
		// Retrieve job executions with tasks
		final MvcResult jobExecutionWithTasks = mvc.perform(
				get("/jobs/" + readJobDefinitionDto.getId() + "/executions/" + readJobExecutionDto.getId())
		).andReturn();
		
		final ReadJobExecutionWithTasksDto readJobExecutionWithTasksDto = objectMapper.readValue(
				jobExecutionWithTasks.getResponse().getContentAsByteArray(),
				ReadJobExecutionWithTasksDto.class
		);
		
		final List<Object> expectedTaskResult = List.of(Map.of("property", "value"));
		final byte[] rawExpectedTaskResultData = objectMapper.writeValueAsString(expectedTaskResult).getBytes();
		when(storageService.retrieve(any())).thenReturn(rawExpectedTaskResultData);
		
		mvc.perform(
				get("/jobs/" + readJobDefinitionDto.getId() + "/executions/" + readJobExecutionDto.getId() + "/tasks/" + readJobExecutionWithTasksDto.getTasks().getFirst().getId() + "/results")
		).andExpect(status().isOk());
	}
	
	ReadJobDefinitionDto createJob() throws Exception {
		final WriteJobDefinitionDto writeJobDefinitionDto = Instancio.create(WriteJobDefinitionDto.class);
		return createJob(writeJobDefinitionDto);
	}
	
	ReadJobDefinitionDto createJob(final WriteJobDefinitionDto writeJobDefinitionDto) throws Exception {
		Thread.sleep(250);
		
		final MvcResult result = mvc.perform(post("/jobs")
				.content(objectMapper.writeValueAsString(writeJobDefinitionDto))
				.contentType(APPLICATION_JSON)
		).andReturn();
		
		final byte[] expectedContent = result.getResponse()
				.getContentAsByteArray();
		
		return objectMapper.readValue(expectedContent, ReadJobDefinitionDto.class);
	}
	
	/**
	 * Should throw an exception when the job definition is not found.
	 */
	@TestTemplate
	void shouldGet404WhenGettingTaskResult1() throws Exception {
		mvc.perform(get("/jobs/123/executions/321/tasks/4/results"))
				.andExpect(status().isNotFound());
	}
	
	/**
	 * Should throw an exception when the job execution does not belong to the job definition.
	 */
	@TestTemplate
	void shouldGet404WhenGettingTaskResult2() throws Exception {
		final ReadJobDefinitionDto readJobDefinitionDto = createJob();
		
		mvc.perform(get("/jobs/" + readJobDefinitionDto.getId() + "/executions/321/tasks/4/results"))
				.andExpect(status().isNotFound());
	}
}