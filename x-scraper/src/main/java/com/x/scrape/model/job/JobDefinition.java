package com.x.scrape.model.job;

import com.x.scrape.model.job.configuration.JobConfiguration;

import java.io.File;
import java.util.UUID;

public class JobDefinition {
	
	private final UUID uuid;
	private final JobConfiguration jobConfiguration;
	
	public JobDefinition(final JobConfiguration jobConfiguration) {
		uuid = UUID.randomUUID();
		this.jobConfiguration = jobConfiguration;
	}
	
	public UUID getUuid() {
		return uuid;
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
		return getJobFolder() + "/job-executions/" + uuid + "/results/tasks";
	}
}
