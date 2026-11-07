package com.beluga.persistence.repository;

import com.beluga.model.job_definition.JobExecution;
import com.beluga.model.job_definition.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface JobExecutionRepository extends JpaRepository<JobExecution, Long> {
	List<JobExecution> findByStatusIn(Collection<JobStatus> statuses);
}
