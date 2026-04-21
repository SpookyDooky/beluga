package com.x.scrape.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.scrape.model.event.storable.payload.Payload;
import com.x.scrape.model.result.ResultFile;
import com.x.scrape.execution.event.task.task_result.TaskResultEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
		componentModel = SPRING
)
@Component
public abstract class ResultFileMapper {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Mapping(target = "path", source = "storageHint.path")
	@Mapping(target = "sizeInBytes", source = "payload")
	@Mapping(target = "fileName", source = "storageHint.fileName")
	public abstract ResultFile map(TaskResultEvent event);
	
	public long mapSizeInBytes(final Payload<?> payload) {
		try {
			if (payload.getData() instanceof InputStream inputStream) {
				return inputStream.readAllBytes().length;
			} else {
				return objectMapper.writeValueAsBytes(payload.getData()).length;
			}
		} catch (final JsonProcessingException e) {
			throw new IllegalStateException("Could not transform data to bytes.", e);
		} catch (final IOException e) {
			throw new IllegalStateException("Could not read InputStream", e);
		}
	}
	
	public String mapPath(final Path path) {
		return path.toString();
	}
}
