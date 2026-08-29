package com.beluga.api.task.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Validated
public class PatchTaskDto {
	
	private Set<@NotNull(message = "A URL must not be null.") URL> add = new HashSet<>();
	private Set<@NotNull(message = "A URL must not be null.") URL> remove = new HashSet<>();
	
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
