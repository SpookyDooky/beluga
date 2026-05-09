package com.beluga.execution.model.task;

import com.beluga.model.job_definition.JobDefinition;
import jakarta.persistence.*;

import java.net.URL;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class TaskDefinition {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	private URL url;
	private boolean active = true;
	
	@ManyToOne
	@JoinColumn(name = "job_definition_id")
	private JobDefinition jobDefinition;
	
	public Long getId() {
		return id;
	}
	
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
	
	public JobDefinition getJobDefinition() {
		return jobDefinition;
	}
	
	public void setJobDefinition(final JobDefinition jobDefinition) {
		this.jobDefinition = jobDefinition;
	}
}
