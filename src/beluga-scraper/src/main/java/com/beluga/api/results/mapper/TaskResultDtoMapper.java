package com.beluga.api.results.mapper;

import com.beluga.api.execution.exception.TaskResultNotFoundException;
import com.beluga.api.results.dto.TaskResultDto;
import com.beluga.execution.model.task.TaskExecution;
import com.beluga.model.result.ResultFile;
import com.beluga.result_storage.ResultStorageService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Path;
import java.util.List;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Component
public class TaskResultDtoMapper {
	
	private static final String DATA_FILE_NAME = "data.json";
	
	private final ObjectMapper objectMapper;
	private final ResultStorageService resultStorageService;
	
	public TaskResultDtoMapper(final ObjectMapper objectMapper,
	                           final ResultStorageService resultStorageService) {
		this.objectMapper = objectMapper;
		this.resultStorageService = resultStorageService;
	}
	
	@Transactional(propagation = MANDATORY)
	public TaskResultDto map(final TaskExecution taskExecution) {
		final ResultFile resultFile = taskExecution.getResultFiles().stream()
				.filter(taskResultFile -> taskResultFile.getKey().equals(DATA_FILE_NAME))
				.findFirst()
				.orElseThrow(TaskResultNotFoundException::new);
		
		final byte[] rawResultData = resultStorageService.retrieve(Path.of(resultFile.getNamespace()));
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
		} catch (final JacksonException e) {
			throw new IllegalStateException(e);
		}
	}
	
}
