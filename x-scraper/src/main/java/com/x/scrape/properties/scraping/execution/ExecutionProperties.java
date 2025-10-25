package com.x.scrape.properties.scraping.execution;

public class ExecutionProperties {
	
	private int workers = 1;
	private int tasksPerSecond = Integer.MAX_VALUE;
	
	public int getWorkers() {
		return workers;
	}
	
	public void setWorkers(final int workers) {
		this.workers = workers;
	}
	
	public int getTasksPerSecond() {
		return tasksPerSecond;
	}
	
	public void setTasksPerSecond(final int tasksPerSecond) {
		this.tasksPerSecond = tasksPerSecond;
	}
}
