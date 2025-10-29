package com.x.scrape.persistence.repository.job;

import com.x.scrape.model.job_definition.JobDefinition;

import java.util.List;
import java.util.Optional;

public interface JobDefinitionRepository {
	
	/**
	 * Persists a job.
	 *
	 * @param jobDefinition the job to persist.
	 * @return the persisted job.
	 */
	JobDefinition save(JobDefinition jobDefinition);
	
	/**
	 * Finds a job by id.
	 *
	 * @param id the id of the job to find.
	 * @return optional including the job if it was found with the specified id, otherwise empty.
	 */
	Optional<JobDefinition> findById(Long id);
	
	/**
	 * Finds all jobs.
	 * @return returns a list containing all jobs.
	 */
	List<JobDefinition> findAll();
}
