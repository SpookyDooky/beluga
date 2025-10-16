package com.x.scrape.model.job;

import com.x.scrape.model.JobConfiguration;

import java.io.File;
import java.util.UUID;

public class Job {
	
	private final UUID id;
	private final JobConfiguration jobConfiguration;
	
	public Job(final JobConfiguration jobConfiguration) {
		id = UUID.randomUUID();
		this.jobConfiguration = jobConfiguration;
	}
	
	public UUID getId() {
		return id;
	}
	
	public JobConfiguration getJobConfiguration() {
		return jobConfiguration;
	}
	
	public void createJobFolders() {
		final File file = new File(getJobTaskResultsFolder());
		file.mkdirs();
	}
	
	public String getJobFolder() {
		return jobConfiguration.getStorageConfiguration().getFolder()
				+ jobConfiguration.getName();
	}
	
	public String getJobTaskResultsFolder() {
		return getJobFolder() + "/job-executions/" + id + "/results/tasks";
	}
}
