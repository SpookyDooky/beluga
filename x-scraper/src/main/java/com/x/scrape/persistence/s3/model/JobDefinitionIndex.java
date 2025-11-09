package com.x.scrape.persistence.s3.model;

import java.util.HashSet;
import java.util.Set;

public class JobDefinitionIndex {
	
	private Set<Long> jobDefinitionIds = new HashSet<>();
	
	public Set<Long> getJobDefinitionIds() {
		return jobDefinitionIds;
	}
	
	public void setJobDefinitionIds(final Set<Long> jobDefinitionIds) {
		this.jobDefinitionIds = jobDefinitionIds;
	}
}
