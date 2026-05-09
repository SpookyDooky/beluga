package com.beluga.mapper.job;

import com.beluga.api.job.dto.read.ReadExecutionConfigurationDto;
import com.beluga.api.job.dto.read.ReadJobDefinitionDto;
import com.beluga.api.job.dto.read.ReadScrapingConfigurationDto;
import com.beluga.api.job.dto.read.ReadStorageConfigurationDto;
import com.beluga.api.job.dto.write.WriteJobDefinitionDto;
import com.beluga.mapper.job.execution.ExecutionConfigurationMapper;
import com.beluga.mapper.job.scraping_configuration.ScrapingConfigurationMapper;
import com.beluga.mapper.job.storage.StorageConfigurationMapper;
import com.beluga.mapper.task.TaskDefinitionMapperService;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.model.job_definition.configuration.execution_configuration.ExecutionDefinition;
import com.beluga.model.job_definition.configuration.scraping_configuration.ScrapingDefinition;
import com.beluga.model.job_definition.configuration.storage_configuration.StorageDefinition;
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
	private ScrapingConfigurationMapper scrapingConfigurationMapper;
	@Mock
	private StorageConfigurationMapper storageConfigurationMapper;
	@Mock
	private ExecutionConfigurationMapper executionConfigurationMapper;
	@Mock
	private TaskDefinitionMapperService taskDefinitionMapperService;
	
	@InjectMocks
	private JobDefinitionMapperImpl mapper;
	
	@Test
	void shouldMap() {
		final JobProperties jobProperties = Instancio.create(JobProperties.class);
		
		final ScrapingDefinition scrapingDefinition = mock();
		when(scrapingConfigurationMapper.map(jobProperties.getScraping())).thenReturn(scrapingDefinition);
		
		final StorageDefinition storageDefinition = mock();
		when(storageConfigurationMapper.map(jobProperties.getStorage())).thenReturn(storageDefinition);
		
		final ExecutionDefinition executionDefinition = mock();
		when(executionConfigurationMapper.map(jobProperties.getExecution())).thenReturn(executionDefinition);
		
		final List<TaskDefinition> taskDefinitions = List.of();
		when(taskDefinitionMapperService.map(jobProperties.getUrl())).thenReturn(taskDefinitions);
		
		final JobDefinition jobDefinition = mapper.map(jobProperties);
		
		assertSame(scrapingDefinition, jobDefinition.getScrapingDefinition());
		assertSame(storageDefinition, jobDefinition.getStorageDefinition());
		assertSame(executionDefinition, jobDefinition.getExecutionDefinition());
		assertSame(taskDefinitions, jobDefinition.getTaskDefinitions());
		assertEquals(jobProperties.getName(), jobDefinition.getName());
	}
	
	@Test
	void shouldMapFromWriteJobDefinitionDto() {
		final WriteJobDefinitionDto dto = Instancio.create(WriteJobDefinitionDto.class);
		
		final ScrapingDefinition scrapingDefinition = mock();
		when(scrapingConfigurationMapper.map(dto.getScraping())).thenReturn(scrapingDefinition);
		
		final StorageDefinition storageDefinition = mock();
		when(storageConfigurationMapper.map(dto.getStorage())).thenReturn(storageDefinition);
		
		final ExecutionDefinition executionDefinition = mock();
		when(executionConfigurationMapper.map(dto.getExecution())).thenReturn(executionDefinition);
		
		final JobDefinition entity = mapper.map(dto);
		
		assertSame(scrapingDefinition, entity.getScrapingDefinition());
		assertSame(storageDefinition, entity.getStorageDefinition());
		assertSame(executionDefinition, entity.getExecutionDefinition());
	}
	
	@Test
	void shouldMapToDto() {
		final JobDefinition jobDefinition = Instancio.create(JobDefinition.class);
		
		final ReadScrapingConfigurationDto readScrapingConfigurationDto = mock();
		when(scrapingConfigurationMapper.map(jobDefinition.getScrapingDefinition())).thenReturn(readScrapingConfigurationDto);
		
		final ReadStorageConfigurationDto readStorageConfigurationDto = mock();
		when(storageConfigurationMapper.map(jobDefinition.getStorageDefinition())).thenReturn(readStorageConfigurationDto);
		
		final ReadExecutionConfigurationDto readExecutionConfigurationDto = mock();
		when(executionConfigurationMapper.map(jobDefinition.getExecutionDefinition())).thenReturn(readExecutionConfigurationDto);
		
		final ReadJobDefinitionDto dto = mapper.map(jobDefinition);
		
		assertEquals(jobDefinition.getId(), dto.getId());
		assertSame(readScrapingConfigurationDto, dto.getScraping());
		assertSame(readStorageConfigurationDto, dto.getStorage());
		assertSame(readExecutionConfigurationDto, dto.getExecution());
	}
	
	@Test
	void shouldUpdate() {
		final WriteJobDefinitionDto dto = Instancio.create(WriteJobDefinitionDto.class);
		final JobDefinition entity = Instancio.create(JobDefinition.class);
		
		mapper.update(dto, entity);
		
		verify(scrapingConfigurationMapper).update(dto.getScraping(), entity.getScrapingDefinition());
		verify(storageConfigurationMapper).update(dto.getStorage(), entity.getStorageDefinition());
		verify(executionConfigurationMapper).update(dto.getExecution(), entity.getExecutionDefinition());
	}
}