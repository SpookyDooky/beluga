package com.x.scrape.api.execution.service;

import com.x.scrape.mapper.job.JobMapper;
import com.x.scrape.service.JobDefinitionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExecutionApiService {
	
	private final JobDefinitionService jobDefinitionService;
	private final JobMapper jobMapper;
	
	public ExecutionApiService(final JobDefinitionService jobDefinitionService,
	                           final JobMapper jobMapper) {
		this.jobDefinitionService = jobDefinitionService;
		this.jobMapper = jobMapper;
	}
	
	@Transactional
	public void start(final Long jobId) {
	
	}
	
	@Transactional
	public void stop(final Long jobId) {
	
	}
	
	@Transactional
	public void pause(final Long jobId) {
	
	}
	
	@Transactional
	public void resume(final Long jobId) {
	
	}
}
