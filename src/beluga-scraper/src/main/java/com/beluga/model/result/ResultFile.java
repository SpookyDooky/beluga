package com.beluga.model.result;

import com.beluga.execution.model.task.TaskExecution;
import jakarta.persistence.*;

import static com.beluga.model.result.CompressionType.NONE;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class ResultFile {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	private String namespace;
	private String key;
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
	
	public String getNamespace() {
		return namespace;
	}
	
	public void setNamespace(final String path) {
		this.namespace = path;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(final String fileName) {
		this.key = fileName;
	}

	@Transient
	public String getResourceIdentifier() {
		return namespace + "/" + key;
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
