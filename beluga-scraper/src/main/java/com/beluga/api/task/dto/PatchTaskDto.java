package com.x.scrape.api.task.dto;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class PatchTaskDto {
	
	private Set<URL> add = new HashSet<>();
	private Set<URL> remove = new HashSet<>();
	
	public Set<URL> getAdd() {
		return add;
	}
	
	public void setAdd(final Set<URL> add) {
		this.add = add;
	}
	
	public Set<URL> getRemove() {
		return remove;
	}
	
	public void setRemove(final Set<URL> remove) {
		this.remove = remove;
	}
}
