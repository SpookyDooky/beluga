package com.x.scrape.persistence.repository.task_execution;

import com.x.scrape.model.task.TaskExecution;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskExecutionPostgreSqlRepository extends TaskExecutionRepository, CrudRepository<TaskExecution, Long> {
}
