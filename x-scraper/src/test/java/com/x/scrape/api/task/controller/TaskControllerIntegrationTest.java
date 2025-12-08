package com.x.scrape.api.task.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.scrape.api.job.dto.read.ReadJobDefinitionDto;
import com.x.scrape.api.job.dto.write.WriteJobDefinitionDto;
import com.x.scrape.api.task.dto.PatchTaskDto;
import com.x.scrape.api.task.dto.ReadTaskDefinitionDto;
import com.x.scrape.api.task.dto.UpdateTaskDto;
import com.x.scrape.integration_test.BaseIntegrationTest;
import org.instancio.Instancio;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	void shouldGetTask() throws Exception {
		final WriteJobDefinitionDto writeJobDefinitionDto = Instancio.create(WriteJobDefinitionDto.class);
		Thread.sleep(250);
		final ReadJobDefinitionDto jobDefinition = createJob(writeJobDefinitionDto);
		
		final UpdateTaskDto updateTaskDto = Instancio.create(UpdateTaskDto.class);
		
		final MvcResult mvcResult = mvc.perform(put("/jobs/" + jobDefinition.getId() + "/tasks")
						.content(objectMapper.writeValueAsString(updateTaskDto))
						.contentType(APPLICATION_JSON)
				).andExpect(status().isOk())
				.andReturn();
		
		final byte[] content = mvcResult.getResponse().getContentAsByteArray();
		final List<ReadTaskDefinitionDto> taskDefinitions = objectMapper.readValue(content, new TypeReference<List<ReadTaskDefinitionDto>>() {
			@Override
			public Type getType() {
				return super.getType();
			}
		});
		
		final ReadTaskDefinitionDto taskDefinition = taskDefinitions.getFirst();
		
		mvc.perform(get("/jobs/" + jobDefinition.getId() + "/tasks/" + taskDefinition.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.url").value(taskDefinition.getUrl().toString()));
	}
	
	@TestTemplate
	void shouldGet404ForRetrievingTaskForNonExistentJob() throws Exception {
		mvc.perform(get("/jobs/123/tasks/321"))
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
	
	@TestTemplate
	void shouldRemoveTasksWhenPatchingTasks() throws Exception {
		final WriteJobDefinitionDto writeJobDefinitionDto = Instancio.create(WriteJobDefinitionDto.class);
		Thread.sleep(250);
		final ReadJobDefinitionDto jobDefinition = createJob(writeJobDefinitionDto);
		final UpdateTaskDto updateTaskDto = Instancio.create(UpdateTaskDto.class);
		
		mvc.perform(put("/jobs/" + jobDefinition.getId() + "/tasks")
				.content(objectMapper.writeValueAsString(updateTaskDto))
				.contentType(APPLICATION_JSON)
		).andExpect(status().isOk());
		
		final PatchTaskDto patchTaskDto = new PatchTaskDto();
		patchTaskDto.setRemove(new HashSet<>(updateTaskDto.getUrls()));
		
		mvc.perform(patch("/jobs/" + jobDefinition.getId() + "/tasks")
				.content(objectMapper.writeValueAsString(patchTaskDto))
				.contentType(APPLICATION_JSON)
		).andExpect(status().isOk());
		
		mvc.perform(get("/jobs/" + jobDefinition.getId() + "/tasks"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(0));
	}
	
	@TestTemplate
	void shouldAddTasksWhenPatchingTasks() throws Exception {
		final WriteJobDefinitionDto writeJobDefinitionDto = Instancio.create(WriteJobDefinitionDto.class);
		Thread.sleep(250);
		final ReadJobDefinitionDto jobDefinition = createJob(writeJobDefinitionDto);
		final PatchTaskDto patchTaskDto = new PatchTaskDto();
		patchTaskDto.setAdd(Set.of(URI.create("http://some.host.com").toURL()));
		
		mvc.perform(patch("/jobs/" + jobDefinition.getId() + "/tasks")
						.content(objectMapper.writeValueAsString(patchTaskDto))
						.contentType(APPLICATION_JSON)
				).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1));
		
		mvc.perform(get("/jobs/" + jobDefinition.getId() + "/tasks"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(0));
	}
	
	@TestTemplate
	void shouldReturn404WhenPatchingTasksForNonExistingJob() throws Exception {
		final PatchTaskDto patchTaskDto = new PatchTaskDto();
		
		mvc.perform(patch("/jobs/123/tasks")
				.content(objectMapper.writeValueAsString(patchTaskDto))
				.contentType(APPLICATION_JSON)
		).andExpect(status().isNotFound());
	}
}
