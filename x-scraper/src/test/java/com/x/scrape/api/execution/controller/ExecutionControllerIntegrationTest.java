package com.x.scrape.api.execution.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.scrape.api.job.dto.read.ReadJobDefinitionDto;
import com.x.scrape.api.job.dto.write.WriteJobDefinitionDto;
import com.x.scrape.api.task.dto.UpdateTaskDto;
import com.x.scrape.integration_test.MultiStoreTest;
import com.x.scrape.result_storage.file_system.FileSystemResultDataStoreProvider;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@MultiStoreTest
@AutoConfigureMockMvc
class ExecutionControllerIntegrationTest {
	
	@MockitoBean(answers = RETURNS_DEEP_STUBS)
	private FileSystemResultDataStoreProvider fileSystemResultDataStoreProvider;
	
	@Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper objectMapper;
	
	@TestTemplate
	void shouldStart() throws Exception {
		final WriteJobDefinitionDto writeJobDefinitionDto = Instancio.create(WriteJobDefinitionDto.class);
		writeJobDefinitionDto.getExecution().setWorkers(1);
		
		final ReadJobDefinitionDto readJobDefinitionDto = createJob(writeJobDefinitionDto);
		
		mvc.perform(post("/jobs/" + readJobDefinitionDto.getId() + "/start"))
				.andExpect(status().isNoContent());
	}
	
	ReadJobDefinitionDto createJob(final WriteJobDefinitionDto writeJobDefinitionDto) throws Exception {
		final MvcResult result = mvc.perform(post("/jobs")
				.content(objectMapper.writeValueAsString(writeJobDefinitionDto))
				.contentType(APPLICATION_JSON)
		).andReturn();
		
		final byte[] expectedContent = result.getResponse().getContentAsByteArray();
		final ReadJobDefinitionDto readJobDefinitionDto = objectMapper.readValue(expectedContent, ReadJobDefinitionDto.class);
		
		addTasks(readJobDefinitionDto.getId());
		
		return readJobDefinitionDto;
	}
	
	void addTasks(final Long jobDefinitionId) throws Exception{
		final UpdateTaskDto updateTaskDto = Instancio.create(UpdateTaskDto.class);
		mvc.perform(put("/jobs/" + jobDefinitionId + "/tasks")
				.content(objectMapper.writeValueAsString(updateTaskDto))
				.contentType(APPLICATION_JSON)
		).andExpect(status().isOk());
	}
	
	@TestTemplate
	void shouldGet404ForStart() throws Exception {
		mvc.perform(post("/jobs/123/start"))
				.andExpect(status().isNotFound());
	}
	
	@TestTemplate
	void shouldStop() throws Exception {
		final WriteJobDefinitionDto writeJobDefinitionDto = Instancio.create(WriteJobDefinitionDto.class);
		writeJobDefinitionDto.getExecution().setWorkers(1);
		
		final ReadJobDefinitionDto readJobDefinitionDto = createJob(writeJobDefinitionDto);
		
		// First start job
		mvc.perform(post("/jobs/" + readJobDefinitionDto.getId() + "/start"))
				.andExpect(status().isNoContent());
		
		// Stop job
		mvc.perform(post("/jobs/" + readJobDefinitionDto.getId() + "/stop"))
				.andExpect(status().isNoContent());
	}
	
	@TestTemplate
	void shouldGet404ForStop() throws Exception {
		mvc.perform(post("/jobs/123/stop"))
				.andExpect(status().isNotFound());
	}
	
	@TestTemplate
	void shouldPause() throws Exception {
		final WriteJobDefinitionDto writeJobDefinitionDto = Instancio.create(WriteJobDefinitionDto.class);
		writeJobDefinitionDto.getExecution().setWorkers(1);
		
		final ReadJobDefinitionDto readJobDefinitionDto = createJob(writeJobDefinitionDto);
		
		mvc.perform(post("/jobs/" + readJobDefinitionDto.getId() + "/pause"))
				.andExpect(status().isNoContent());
	}
	
	@TestTemplate
	void shouldGet404ForPause() throws Exception{
		mvc.perform(post("/jobs/123/pause"))
				.andExpect(status().isNotFound());
	}
	
	@TestTemplate
	void shouldResume() throws Exception {
		final WriteJobDefinitionDto writeJobDefinitionDto = Instancio.create(WriteJobDefinitionDto.class);
		writeJobDefinitionDto.getExecution().setWorkers(1);
		
		final ReadJobDefinitionDto readJobDefinitionDto = createJob(writeJobDefinitionDto);
		
		mvc.perform(post("/jobs/" + readJobDefinitionDto.getId() + "/resume"))
				.andExpect(status().isNoContent());
	}
	
	@TestTemplate
	void shouldGet404ForResume() throws Exception {
		mvc.perform(post("/jobs/123/resume"))
				.andExpect(status().isNotFound());
	}
	
	@TestTemplate
	void shouldGetLatestExecution() {
		Assertions.fail();
	}
	
	@TestTemplate
	void shouldGet404ForRetrievingLatestExecutionIfJobDoesNotExist() {
		Assertions.fail();
	}
	
	@TestTemplate
	void shouldGet404ForRetrievingLatestExecutionIfJobHasNoExecutions() {
		Assertions.fail();
	}
	
	@TestTemplate
	void shouldRetrieveExecutions() {
		Assertions.fail();
	}
	
	@TestTemplate
	void shouldGet404ForRetrievingExecutionsForNonExistingJob() {
		Assertions.fail();
	}
	
	@TestTemplate
	void shouldGetExecution() {
		Assertions.fail();
	}
	
	@TestTemplate
	void shouldGet404ForRetrievingExecutionOfNonExistingJob() {
		Assertions.fail();
	}
	
	@TestTemplate
	void shouldGet404ForRetrievingNonExistingExecutionFromJob() {
		Assertions.fail();
	}
}