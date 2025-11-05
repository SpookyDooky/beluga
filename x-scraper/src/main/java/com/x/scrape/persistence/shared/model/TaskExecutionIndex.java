package com.x.scrape.persistence.shared.model;

import java.util.HashSet;
import java.util.Set;

public class TaskExecutionIndex {
	
	private Set<Long> ids = new HashSet<>();
	
	public Set<Long> getIds() {
		return ids;
	}
	
	public void setIds(final Set<Long> ids) {
		this.ids = ids;
	}
}
