package com.beluga.persistence.repository;

import com.beluga.execution.model.task.TaskExecution;
import com.beluga.model.job_definition.JobExecution;
import com.beluga.model.result.ResultFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@SuppressWarnings("PMD.MethodNamingConventions")
public interface ResultFileRepository extends CrudRepository<ResultFile, Long> {

    Optional<ResultFile> findByTaskExecutionAndKey(TaskExecution taskExecution, String key);
    Page<ResultFile> findByTaskExecution_JobExecutionAndKey(JobExecution jobExecution, String key, Pageable pageable);
}
