package com.x.scrape.execution.model.job;

public class ExecutionConfiguration {
	
	private int workers;
	private double tasksPerSecond;
	
	public int getWorkers() {
		return workers;
	}
	
	public void setWorkers(final int workers) {
		this.workers = workers;
	}
	
	public double getTasksPerSecond() {
		return tasksPerSecond;
	}
	
	public void setTasksPerSecond(final double tasksPerSecond) {
		this.tasksPerSecond = tasksPerSecond;
	}
}
