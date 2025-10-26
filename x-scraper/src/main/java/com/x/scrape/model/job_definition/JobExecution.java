package com.x.scrape.model.job_definition;

import com.x.scrape.persistence.shared.model.HasId;

import java.time.Instant;

public class JobExecution implements HasId {
	
	private Long id;
	private final Instant executedAt = Instant.now();
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(final Long id) {
		this.id = id;
	}
	
	public Instant getExecutedAt() {
		return executedAt;
	}
}
