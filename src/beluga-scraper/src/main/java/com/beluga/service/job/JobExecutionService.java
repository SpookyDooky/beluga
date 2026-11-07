package com.beluga.service.job;

import com.beluga.model.job_definition.JobExecution;
import com.beluga.model.job_definition.JobStatus;
import com.beluga.persistence.repository.JobExecutionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Service
public class JobExecutionService {

	private final JobExecutionRepository jobExecutionRepository;

	public JobExecutionService(final JobExecutionRepository jobExecutionRepository) {
		this.jobExecutionRepository = jobExecutionRepository;
	}

	/**
	 * Retrieves all {@link JobExecution}'s that are active or planned, these are the ones that still need to run.
	 *
	 * @return the {@link JobExecution}'s.
	 */
	@Transactional(propagation = MANDATORY)
	public List<JobExecution> findAllByStatus(final Collection<JobStatus> statuses) {
		return jobExecutionRepository.findByStatusIn(statuses);
	}
}
