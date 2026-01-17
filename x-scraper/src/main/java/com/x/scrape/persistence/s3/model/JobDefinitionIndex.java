package com.x.scrape.persistence.s3.model;

import java.util.HashSet;
import java.util.Set;

@Deprecated(forRemoval = true) // Due to SQL lite being a much better alternative
public class JobDefinitionIndex {
	
	private Set<Long> jobDefinitionIds = new HashSet<>();
	
	public Set<Long> getJobDefinitionIds() {
		return jobDefinitionIds;
	}
	
	public void setJobDefinitionIds(final Set<Long> jobDefinitionIds) {
		this.jobDefinitionIds = jobDefinitionIds;
	}
}
