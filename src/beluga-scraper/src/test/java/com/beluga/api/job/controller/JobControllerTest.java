package com.beluga.api.job.controller;

import com.beluga.api.job.dto.read.ReadJobDefinitionDto;
import com.beluga.api.job.dto.write.WriteJobDefinitionDto;
import com.beluga.api.job.validation.validator.JobNameValidator;
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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobControllerTest {
	
	@Mock
	private ContextLogger logger;
	@Mock
	private JobNameValidator jobNameValidator;
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
		when(jobDefinitionService.getById(jobDefinition.getId())).thenReturn(jobDefinition);
		
		final ReadJobDefinitionDto readJobDefinitionDto = mock();
		when(jobDefinitionMapper.map(jobDefinition)).thenReturn(readJobDefinitionDto);
		
		final ReadJobDefinitionDto result = jobController.get(jobDefinition.getId());

		assertSame(readJobDefinitionDto, result);
	}
	
	@Test
	void shouldUpdate() {
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		when(jobDefinitionService.getById(jobDefinition.getId())).thenReturn(jobDefinition);
		
		final ReadJobDefinitionDto readJobDefinitionDto = mock();
		when(jobDefinitionMapper.map(jobDefinition)).thenReturn(readJobDefinitionDto);
		
		final WriteJobDefinitionDto writeJobDefinitionDto = mock();
		final ReadJobDefinitionDto result = jobController.update(jobDefinition.getId(), writeJobDefinitionDto);
		
		verify(jobDefinitionMapper).update(writeJobDefinitionDto, jobDefinition);
		verify(jobDefinitionService).save(jobDefinition);
		
		assertSame(readJobDefinitionDto, result);
	}
}