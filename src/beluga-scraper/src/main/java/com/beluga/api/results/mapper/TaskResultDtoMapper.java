package com.beluga.api.results.mapper;

import com.beluga.api.results.dto.TaskResultDto;
import com.beluga.model.result.ResultFile;
import com.beluga.result_storage.ResultStorageService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Component
public class TaskResultDtoMapper {

	private final ObjectMapper objectMapper;
	private final ResultStorageService resultStorageService;
	
	public TaskResultDtoMapper(final ObjectMapper objectMapper,
	                           final ResultStorageService resultStorageService) {
		this.objectMapper = objectMapper;
		this.resultStorageService = resultStorageService;
	}
	
	@Transactional(propagation = MANDATORY)
	public TaskResultDto map(final ResultFile resultFile) {
		final byte[] rawResultData = resultStorageService.retrieve(resultFile.getResourceIdentifier());
		final List<Object> data = mapData(rawResultData);
		
		final TaskResultDto taskResultDto = new TaskResultDto();
		
		taskResultDto.setTaskId(resultFile.getTaskExecution().getId());
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
