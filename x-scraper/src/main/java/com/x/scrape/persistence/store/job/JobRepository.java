package com.x.scrape.persistence.store.job;

import com.x.scrape.model.job.Job;

import java.util.List;
import java.util.Optional;

public interface JobRepository {
	
	/**
	 * Persists a job.
	 *
	 * @param job the job to persist.
	 * @return the persisted job.
	 */
	Job save(Job job);
	
	/**
	 * Finds a job by id.
	 *
	 * @param id the id of the job to find.
	 * @return optional including the job if it was found with the specified id, otherwise empty.
	 */
	Optional<Job> findById(Long id);
	
	/**
	 * Finds all jobs.
	 * @return returns a list containing all jobs.
	 */
	List<Job> findAll();
}
