package com.x.scrape.model.job_definition;

import com.x.scrape.persistence.shared.model.HasId;
import jakarta.persistence.*;

import java.time.Instant;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class JobExecution implements HasId {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private Instant executedAt = Instant.now();
	
	@ManyToOne
	@JoinColumn(name = "job_definition_id")
	private JobDefinition jobDefinition;
	
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
	
	public void setExecutedAt(final Instant executedAt) {
		this.executedAt = executedAt;
	}
	
	public JobDefinition getJobDefinition() {
		return jobDefinition;
	}
	
	public void setJobDefinition(final JobDefinition jobDefinition) {
		this.jobDefinition = jobDefinition;
	}
}
