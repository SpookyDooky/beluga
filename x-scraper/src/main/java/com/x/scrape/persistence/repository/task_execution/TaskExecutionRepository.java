package com.x.scrape.persistence.repository.task_execution;

import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.model.task.TaskExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TaskExecutionRepository {
	TaskExecution save(TaskExecution taskExecution);
	Optional<TaskExecution> findById(Long id);
	Page<TaskExecution> findByJobExecution(JobExecution jobExecution, Pageable pageable);
}
