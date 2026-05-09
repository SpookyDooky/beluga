package com.beluga.api.execution.dto;

import com.beluga.model.job_definition.JobStatus;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class ReadJobExecutionDto {

	@NotNull
	private Long id;
	@NotNull
	private Instant executedAt;
	@NotNull
	private JobStatus status;
	
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
	
}
