package com.beluga.properties.scraping.execution;

public class ExecutionProperties {

	/**
	 * The amount of workers to use for a job, default 1.
	 */
	private int workers = 1;
	/**
	 * Maximum tasks per second for a job, default 1.
	 */
	private int tasksPerSecond = 1;
	
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
