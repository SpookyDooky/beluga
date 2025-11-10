package com.x.scrape.persistence.repository.task_execution;

import com.x.scrape.model.task.TaskExecution;

import java.util.Optional;

public interface TaskExecutionRepository {
	TaskExecution save(TaskExecution taskExecution);
	Optional<TaskExecution> findById(Long id);
}
