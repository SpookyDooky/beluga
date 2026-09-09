package com.beluga.properties.scraping.execution;

public class ExecutionProperties {

	/**
	 * Maximum tasks per second for a job, default 1.
	 */
	private int tasksPerSecond = 1;

	public int getTasksPerSecond() {
		return tasksPerSecond;
	}
	
	public void setTasksPerSecond(final int tasksPerSecond) {
		this.tasksPerSecond = tasksPerSecond;
	}
}
