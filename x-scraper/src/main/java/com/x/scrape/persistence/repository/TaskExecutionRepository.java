package com.x.scrape.persistence.repository;

import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.model.task.TaskExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskExecutionRepository extends CrudRepository<TaskExecution, Long> {
	Page<TaskExecution> findByJobExecution(JobExecution jobExecution, Pageable pageable);
}
