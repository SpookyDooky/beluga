package com.x.scrape.model.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.persistence.shared.model.HasId;
import jakarta.persistence.*;

import java.net.URL;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class TaskDefinition implements HasId {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	private URL url;
	private boolean active;
	
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
	
	public URL getUrl() {
		return url;
	}
	
	public void setUrl(final URL url) {
		this.url = url;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(final boolean active) {
		this.active = active;
	}
	
	@JsonIgnore
	public JobDefinition getJobDefinition() {
		return jobDefinition;
	}
	
	public void setJobDefinition(final JobDefinition jobDefinition) {
		this.jobDefinition = jobDefinition;
	}
}
