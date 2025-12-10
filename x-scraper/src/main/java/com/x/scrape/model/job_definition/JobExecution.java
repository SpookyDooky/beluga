package com.x.scrape.model.job_definition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.x.scrape.model.task.TaskExecution;
import com.x.scrape.persistence.shared.model.HasId;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.x.scrape.model.job_definition.JobStatus.PLANNED;
import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class JobExecution implements HasId {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private Instant executedAt = Instant.now();
	
	@Enumerated(STRING)
	private JobStatus status = PLANNED;
	
	@ManyToOne
	@JoinColumn(name = "job_definition_id")
	private JobDefinition jobDefinition;
	
	@OneToMany(
			cascade = ALL,
			mappedBy = "jobExecution"
	)
	private List<TaskExecution> tasks = new ArrayList<>();
	
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
	
	public JobStatus getStatus() {
		return status;
	}
	
	public void setStatus(final JobStatus status) {
		this.status = status;
	}
	
	@JsonIgnore
	public JobDefinition getJobDefinition() {
		return jobDefinition;
	}
	
	public void setJobDefinition(final JobDefinition jobDefinition) {
		this.jobDefinition = jobDefinition;
	}
	
	@JsonIgnore
	public List<TaskExecution> getTasks() {
		return tasks;
	}
	
	public void setTasks(final List<TaskExecution> tasks) {
		tasks.forEach(this::addTask);
	}
	
	public void addTask(final TaskExecution task) {
		task.setJobExecution(this);
		tasks.add(task);
	}
}
