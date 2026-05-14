package com.beluga.properties;

import com.beluga.properties.datastore.ResultStorageProperties;
import com.beluga.properties.persistence.PersistenceProperties;
import com.beluga.properties.scraping.JobProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@ConfigurationProperties("beluga")
@Validated
public class BelugaScraperProperties {
	
	@NotNull
	@Valid
	private PersistenceProperties persistence;
	
	@Valid
	private ResultStorageProperties resultStorage = new ResultStorageProperties();
	
	private List<@NotNull @Valid JobProperties> jobs;
	
	public PersistenceProperties getPersistence() {
		return persistence;
	}
	
	public void setPersistence(final PersistenceProperties persistence) {
		this.persistence = persistence;
	}
	
	public ResultStorageProperties getResultStorage() {
		return resultStorage;
	}
	
	public void setResultStorage(final ResultStorageProperties resultStorage) {
		this.resultStorage = resultStorage;
	}
	
	public List<JobProperties> getJobs() {
		return jobs;
	}
	
	public void setJobs(final List<JobProperties> jobs) {
		this.jobs = jobs;
	}
}
