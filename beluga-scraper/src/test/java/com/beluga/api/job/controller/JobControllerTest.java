package com.beluga.api.job.controller;

import com.beluga.api.job.dto.read.ReadJobDefinitionDto;
import com.beluga.api.job.dto.write.WriteJobDefinitionDto;
import com.beluga.logging.ContextLogger;
import com.beluga.mapper.job.JobDefinitionMapper;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.service.job.JobDefinitionService;
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
		
		final ReadJobDefinitionDto result = jobController.create(writeDto);
		
		verify(jobDefinitionService).save(entity);
		assertSame(readDto, result);
	}
	
	@Test
	void shouldGetJob() {
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		when(jobDefinitionService.findById(jobDefinition.getId())).thenReturn(Optional.of(jobDefinition));
		
		final ReadJobDefinitionDto readJobDefinitionDto = mock();
		when(jobDefinitionMapper.map(jobDefinition)).thenReturn(readJobDefinitionDto);
		
		final ResponseEntity<ReadJobDefinitionDto> result = jobController.get(jobDefinition.getId());
		
		assertSame(readJobDefinitionDto, result.getBody());
	}
	
	@Test
	void shouldGetJobReturnsNotFound() {
		final Long id = 123L;
		when(jobDefinitionService.findById(id)).thenReturn(Optional.empty());
		
		final ResponseEntity<ReadJobDefinitionDto> result = jobController.get(id);
		
		assertEquals(404, result.getStatusCode().value());
	}
	
	@Test
	void shouldUpdate() {
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		when(jobDefinitionService.findById(jobDefinition.getId())).thenReturn(Optional.of(jobDefinition));
		
		final ReadJobDefinitionDto readJobDefinitionDto = mock();
		when(jobDefinitionMapper.map(jobDefinition)).thenReturn(readJobDefinitionDto);
		
		final WriteJobDefinitionDto writeJobDefinitionDto = mock();
		final ResponseEntity<ReadJobDefinitionDto> result = jobController.update(jobDefinition.getId(), writeJobDefinitionDto);
		
		verify(jobDefinitionMapper).update(writeJobDefinitionDto, jobDefinition);
		verify(jobDefinitionService).save(jobDefinition);
		
		assertSame(readJobDefinitionDto, result.getBody());
	}
	
	@Test
	void shouldUpdateReturnsNotFound() {
		final Long id = 123L;
		when(jobDefinitionService.findById(id)).thenReturn(Optional.empty());
		
		final ResponseEntity<ReadJobDefinitionDto> result = jobController.update(id, null);
		
		assertEquals(404, result.getStatusCode().value());
	}
}