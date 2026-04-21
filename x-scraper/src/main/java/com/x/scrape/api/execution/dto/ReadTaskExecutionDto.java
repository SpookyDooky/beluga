package com.x.scrape.api.execution.dto;

import com.x.scrape.execution.model.task.TaskStatus;
import jakarta.validation.constraints.NotNull;

import java.net.URL;
import java.time.Instant;

public class ReadTaskExecutionDto {
	
	@NotNull
	private Long id;
	@NotNull
	private Instant executedAt;
	@NotNull
	private TaskStatus status;
	@NotNull
	private URL url;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public Instant getExecutedAt() {
		return executedAt;
	}
	
	public void setExecutedAt(final Instant executedAt) {
		this.executedAt = executedAt;
	}
	
	public TaskStatus getStatus() {
		return status;
	}
	
	public void setStatus(final TaskStatus status) {
		this.status = status;
	}
	
	public URL getUrl() {
		return url;
	}
	
	public void setUrl(final URL url) {
		this.url = url;
	}
}
