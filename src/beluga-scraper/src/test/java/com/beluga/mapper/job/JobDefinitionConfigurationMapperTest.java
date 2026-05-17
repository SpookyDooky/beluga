package com.beluga.mapper.job;

import com.beluga.api.job.dto.read.ReadExecutionConfigurationDto;
import com.beluga.api.job.dto.read.ReadJobDefinitionDto;
import com.beluga.api.job.dto.read.ReadScrapingConfigurationDto;
import com.beluga.api.job.dto.write.WriteJobDefinitionDto;
import com.beluga.mapper.job.execution.ExecutionDefinitionMapper;
import com.beluga.mapper.job.scraping_configuration.ScrapingDefinitionMapper;
import com.beluga.mapper.task.TaskDefinitionMapperService;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.model.job_definition.configuration.execution_configuration.ExecutionDefinition;
import com.beluga.model.job_definition.configuration.scraping_configuration.ScrapingDefinition;
import com.beluga.execution.model.task.TaskDefinition;
import com.beluga.properties.scraping.JobProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobDefinitionConfigurationMapperTest {
	
	@Mock
	private ScrapingDefinitionMapper scrapingDefinitionMapper;
	@Mock
	private ExecutionDefinitionMapper executionDefinitionMapper;
	@Mock
	private TaskDefinitionMapperService taskDefinitionMapperService;
	
	@InjectMocks
	private JobDefinitionMapperImpl mapper;
	
	@Test
	void shouldMap() {
		final JobProperties jobProperties = Instancio.create(JobProperties.class);
		
		final ScrapingDefinition scrapingDefinition = mock();
		when(scrapingDefinitionMapper.map(jobProperties.getScraping())).thenReturn(scrapingDefinition);
		
		final ExecutionDefinition executionDefinition = mock();
		when(executionDefinitionMapper.map(jobProperties.getExecution())).thenReturn(executionDefinition);
		
		final List<TaskDefinition> taskDefinitions = List.of();
		when(taskDefinitionMapperService.map(jobProperties.getUrl())).thenReturn(taskDefinitions);
		
		final JobDefinition jobDefinition = mapper.map(jobProperties);
		
		assertSame(scrapingDefinition, jobDefinition.getScrapingDefinition());
		assertSame(executionDefinition, jobDefinition.getExecutionDefinition());
		assertSame(taskDefinitions, jobDefinition.getTaskDefinitions());
		assertEquals(jobProperties.getName(), jobDefinition.getName());
	}
	
	@Test
	void shouldMapFromWriteJobDefinitionDto() {
		final WriteJobDefinitionDto dto = Instancio.create(WriteJobDefinitionDto.class);
		
		final ScrapingDefinition scrapingDefinition = mock();
		when(scrapingDefinitionMapper.map(dto.getScraping())).thenReturn(scrapingDefinition);
		
		final ExecutionDefinition executionDefinition = mock();
		when(executionDefinitionMapper.map(dto.getExecution())).thenReturn(executionDefinition);
		
		final JobDefinition entity = mapper.map(dto);
		
		assertSame(scrapingDefinition, entity.getScrapingDefinition());
		assertSame(executionDefinition, entity.getExecutionDefinition());
	}
	
	@Test
	void shouldMapToDto() {
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		
		final ReadScrapingConfigurationDto readScrapingConfigurationDto = mock();
		when(scrapingDefinitionMapper.map(jobDefinition.getScrapingDefinition())).thenReturn(readScrapingConfigurationDto);
		
		final ReadExecutionConfigurationDto readExecutionConfigurationDto = mock();
		when(executionDefinitionMapper.map(jobDefinition.getExecutionDefinition())).thenReturn(readExecutionConfigurationDto);
		
		final ReadJobDefinitionDto dto = mapper.map(jobDefinition);
		
		assertEquals(jobDefinition.getId(), dto.getId());
		assertSame(readScrapingConfigurationDto, dto.getScraping());
		assertSame(readExecutionConfigurationDto, dto.getExecution());
	}
	
	@Test
	void shouldUpdate() {
		final WriteJobDefinitionDto dto = Instancio.create(WriteJobDefinitionDto.class);
		final JobDefinition entity = Instancio.create(JobDefinition.class);
		
		mapper.update(dto, entity);
		
		verify(scrapingDefinitionMapper).update(dto.getScraping(), entity.getScrapingDefinition());
		verify(executionDefinitionMapper).update(dto.getExecution(), entity.getExecutionDefinition());
	}
}