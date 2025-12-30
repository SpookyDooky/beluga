package com.x.scrape.model.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.x.scrape.model.task.TaskExecution;
import com.x.scrape.persistence.shared.model.HasId;
import jakarta.persistence.*;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class ResultFile implements HasId {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	private String path;
	private Long sizeInBytes;
	
	@Enumerated(STRING)
	private CompressionType compressionType;
	
	@ManyToOne
	@JoinColumn(name = "task_execution_id")
	private TaskExecution taskExecution;
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(final Long id) {
		this.id = id;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(final String path) {
		this.path = path;
	}
	
	public Long getSizeInBytes() {
		return sizeInBytes;
	}
	
	public void setSizeInBytes(final Long sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
	}
	
	public CompressionType getCompressionType() {
		return compressionType;
	}
	
	public void setCompressionType(final CompressionType compressionType) {
		this.compressionType = compressionType;
	}
	
	@JsonIgnore
	public TaskExecution getTaskExecution() {
		return taskExecution;
	}
	
	public void setTaskExecution(final TaskExecution taskExecution) {
		this.taskExecution = taskExecution;
	}
}
