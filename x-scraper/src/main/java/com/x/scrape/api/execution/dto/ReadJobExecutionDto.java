package com.x.scrape.api.execution.dto;

import com.x.scrape.model.job_definition.JobStatus;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ReadJobExecutionDto {

	@NotNull
	private Long id;
	@NotNull
	private Instant executedAt;
	@NotNull
	private JobStatus status;
	
	private List<@NotNull ReadTaskExecutionDto> tasks = new ArrayList<>();
	
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
	
	public JobStatus getStatus() {
		return status;
	}
	
	public void setStatus(final JobStatus status) {
		this.status = status;
	}
	
	public List<ReadTaskExecutionDto> getTasks() {
		return tasks;
	}
	
	public void setTasks(final List<ReadTaskExecutionDto> tasks) {
		this.tasks = tasks;
	}
}
