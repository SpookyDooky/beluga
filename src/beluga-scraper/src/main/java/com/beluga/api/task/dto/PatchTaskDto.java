package com.beluga.api.task.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Validated
public class PatchTaskDto {
	
	private List<@NotNull(message = "A URL must not be null.") URL> add = new ArrayList<>();
	private List<@NotNull(message = "A URL must not be null.") URL> remove = new ArrayList<>();
	
	public List<URL> getAdd() {
		return add;
	}
	
	public void setAdd(final List<URL> add) {
		this.add = add;
	}
	
	public List<URL> getRemove() {
		return remove;
	}
	
	public void setRemove(final List<URL> remove) {
		this.remove = remove;
	}
}
