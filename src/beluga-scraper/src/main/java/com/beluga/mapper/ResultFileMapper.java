package com.beluga.mapper;

import com.beluga.execution.event.task.task_result.TaskResultStoredEvent;
import com.beluga.model.event.storable.payload.Payload;
import com.beluga.model.result.ResultFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

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

	@Mapping(target = "key", source = "key")
	@Mapping(target = "sizeInBytes", source = "payload")
	@Mapping(target = "namespace", source = "namespace")
	public abstract ResultFile map(TaskResultStoredEvent event);
	
	public long mapSizeInBytes(final Payload<?> payload) {
		try {
			if (payload.getData() instanceof InputStream inputStream) {
				return inputStream.readAllBytes().length;
			} else {
				return objectMapper.writeValueAsBytes(payload.getData()).length;
			}
		} catch (final JacksonException e) {
			throw new IllegalStateException("Could not transform data to bytes.", e);
		} catch (final IOException e) {
			throw new IllegalStateException("Could not read InputStream", e);
		}
	}
	
	public String mapPath(final Path path) {
		return path.toString();
	}
}
