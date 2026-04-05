package com.x.scrape.model.result;

import com.x.scrape.model.task.TaskExecution;
import jakarta.persistence.*;

import static com.x.scrape.model.result.CompressionType.NONE;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class ResultFile {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	private String path;
	private String fileName;
	private Long sizeInBytes;
	
	@Enumerated(STRING)
	private CompressionType compressionType = NONE;
	
	@ManyToOne
	@JoinColumn(name = "task_execution_id")
	private TaskExecution taskExecution;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(final String path) {
		this.path = path;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(final String fileName) {
		this.fileName = fileName;
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
	
	public TaskExecution getTaskExecution() {
		return taskExecution;
	}
	
	public void setTaskExecution(final TaskExecution taskExecution)  {
		this.taskExecution = taskExecution;
	}
}
