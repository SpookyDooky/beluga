package com.beluga.persistence.repository;

import com.beluga.execution.model.task.TaskExecution;
import com.beluga.execution.model.task.TaskStatus;
import com.beluga.model.job_definition.JobExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TaskExecutionRepository extends JpaRepository<TaskExecution, Long> {
	Page<TaskExecution> findByJobExecution(JobExecution jobExecution, Pageable pageable);
	List<TaskExecution> findByJobExecutionAndStatusIn(JobExecution jobExecution, Collection<TaskStatus> statuses);
}
