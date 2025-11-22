package com.x.scrape.api.job.controller;

import com.x.scrape.api.job.dto.read.ReadJobDefinitionDto;
import com.x.scrape.api.job.dto.write.WriteJobDefinitionDto;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.mapper.job.JobDefinitionMapper;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.service.JobDefinitionService;
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
}