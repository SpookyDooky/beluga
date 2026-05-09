package com.beluga.properties;

import com.beluga.properties.datastore.ResultDataStoreProperties;
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
	private ResultDataStoreProperties resultDatastore = new ResultDataStoreProperties();
	
	private List<@NotNull @Valid JobProperties> jobs;
	
	public PersistenceProperties getPersistence() {
		return persistence;
	}
	
	public void setPersistence(final PersistenceProperties persistence) {
		this.persistence = persistence;
	}
	
	public ResultDataStoreProperties getResultDatastore() {
		return resultDatastore;
	}
	
	public void setResultDatastore(final ResultDataStoreProperties resultDatastore) {
		this.resultDatastore = resultDatastore;
	}
	
	public List<JobProperties> getJobs() {
		return jobs;
	}
	
	public void setJobs(final List<JobProperties> jobs) {
		this.jobs = jobs;
	}
}
