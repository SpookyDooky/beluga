package com.x.scrape.model.job_definition;

import com.x.scrape.persistence.shared.model.HasId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.Instant;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class JobExecution implements HasId {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
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
