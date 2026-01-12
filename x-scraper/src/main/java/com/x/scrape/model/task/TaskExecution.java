package com.x.scrape.model.task;

import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.model.result.ResultFile;
import com.x.scrape.persistence.shared.model.HasId;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.x.scrape.model.task.TaskStatus.PLANNED;
import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class TaskExecution implements HasId {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private Instant executedAt;
	
	@Enumerated(STRING)
	private TaskStatus status = PLANNED;
	private String resultFolder;
	
	@ManyToOne
	@JoinColumn(name = "task_definition_id")
	private TaskDefinition taskDefinition;
	
	@ManyToOne
	@JoinColumn(name = "job_execution_id")
	private JobExecution jobExecution;
	
	@OneToMany(
			cascade = ALL,
			mappedBy = "taskExecution"
	)
	private final List<ResultFile> resultFiles = new ArrayList<>();
	
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
	
	public TaskStatus getStatus() {
		return status;
	}
	
	public void setStatus(final TaskStatus status) {
		this.status = status;
	}
	
	public String getResultFolder() {
		return resultFolder;
	}
	
	public void setResultFolder(final String resultFolder) {
		this.resultFolder = resultFolder;
	}
	
	public TaskDefinition getTaskDefinition() {
		return taskDefinition;
	}
	
	public void setTaskDefinition(final TaskDefinition taskDefinition) {
		this.taskDefinition = taskDefinition;
	}
	
	public JobExecution getJobExecution() {
		return jobExecution;
	}
	
	public void setJobExecution(final JobExecution jobExecution) {
		this.jobExecution = jobExecution;
	}
	
	public List<ResultFile> getResultFiles() {
		return resultFiles;
	}
	
	public void setResultFiles(final List<ResultFile> resultFiles) {
		this.resultFiles.clear();
		resultFiles.forEach(resultFile -> {
			resultFile.setTaskExecution(this);
			this.resultFiles.add(resultFile);
		});
	}
	
	public void addResultFile(final ResultFile resultFile) {
		resultFiles.add(resultFile);
		resultFile.setTaskExecution(this);
	}
}
