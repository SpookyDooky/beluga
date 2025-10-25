package com.x.scrape.persistence.store.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.scrape.model.job.Job;
import com.x.scrape.persistence.config.conditionals.IsFileSystem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@IsFileSystem
@Component
public class JobFileSystemRepository implements JobRepository {
	
	private static final String JOB_PERSISTENCE_PATH = "/jobs";
	
	private final ObjectMapper objectMapper;
	
	public JobFileSystemRepository(final ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	@Override
	public Job save(final Job job) {
		return null;
	}
	
	@Override
	public Optional<Job> findById(final Long id) {
		return Optional.empty();
	}
	
	@Override
	public List<Job> findAll() {
		return null;
	}
}
