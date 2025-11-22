package com.x.scrape.api.job.controller;

import com.x.scrape.api.job.dto.read.ReadJobDefinitionDto;
import com.x.scrape.api.job.dto.write.WriteJobDefinitionDto;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.mapper.job.JobDefinitionMapper;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.service.JobDefinitionService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobControllerTest {
	
	@Mock
	private ContextLogger logger;
	@Mock
	private JobDefinitionMapper jobDefinitionMapper;
	@Mock
	private JobDefinitionService jobDefinitionService;
	
	@InjectMocks
	private JobController jobController;
	
	@Test
	void shouldCreateJob() {
		final WriteJobDefinitionDto writeDto = mock();
		
		final JobDefinition entity = mock();
		when(jobDefinitionMapper.map(writeDto)).thenReturn(entity);
		
		final ReadJobDefinitionDto readDto = mock();
		when(jobDefinitionMapper.map(entity)).thenReturn(readDto);
		
		final ReadJobDefinitionDto result = jobController.createJob(writeDto);
		
		verify(jobDefinitionService).save(entity);
		assertSame(readDto, result);
	}
	
	@Test
	void shouldGetJob() {
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		when(jobDefinitionService.findById(jobDefinition.getId())).thenReturn(Optional.of(jobDefinition));
		
		final ReadJobDefinitionDto readJobDefinitionDto = mock();
		when(jobDefinitionMapper.map(jobDefinition)).thenReturn(readJobDefinitionDto);
		
		final ResponseEntity<ReadJobDefinitionDto> result = jobController.getJob(jobDefinition.getId());
		
		assertSame(readJobDefinitionDto, result.getBody());
	}
	
	@Test
	void shouldGetJobReturnsNotFound() {
		final Long id = 123L;
		when(jobDefinitionService.findById(id)).thenReturn(Optional.empty());
		
		final ResponseEntity<ReadJobDefinitionDto> result = jobController.getJob(id);
		
		assertEquals(404, result.getStatusCode().value());
	}
}