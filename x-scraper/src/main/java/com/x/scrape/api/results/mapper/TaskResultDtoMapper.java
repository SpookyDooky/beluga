package com.x.scrape.api.results.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.scrape.api.execution.exception.TaskResultNotFoundException;
import com.x.scrape.api.results.dto.TaskResultDto;
import com.x.scrape.model.result.ResultFile;
import com.x.scrape.execution.model.task.TaskExecution;
import com.x.scrape.result_storage.StorageService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Component
public class TaskResultDtoMapper {
	
	private static final String DATA_FILE_NAME = "data.json";
	
	private final ObjectMapper objectMapper;
	private final StorageService storageService;
	
	public TaskResultDtoMapper(final ObjectMapper objectMapper,
	                           final StorageService storageService) {
		this.objectMapper = objectMapper;
		this.storageService = storageService;
	}
	
	@Transactional(propagation = MANDATORY)
	public TaskResultDto map(final TaskExecution taskExecution) {
		final ResultFile resultFile = taskExecution.getResultFiles().stream()
				.filter(taskResultFile -> taskResultFile.getFileName().equals(DATA_FILE_NAME))
				.findFirst()
				.orElseThrow(TaskResultNotFoundException::new);
		
		final byte[] rawResultData = storageService.retrieve(Path.of(resultFile.getPath()));
		final List<Object> data = mapData(rawResultData);
		
		final TaskResultDto taskResultDto = new TaskResultDto();
		
		taskResultDto.setTaskId(taskExecution.getId());
		taskResultDto.setData(data);
		
		return taskResultDto;
	}
	
	@SuppressWarnings("unchecked")
	private List<Object> mapData(final byte[] rawData) {
		try {
			return objectMapper.readValue(rawData, List.class);
		} catch (final IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
}
