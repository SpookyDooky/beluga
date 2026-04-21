package com.x.scrape.model.task;

import com.x.scrape.execution.model.job.Job;
import com.x.scrape.logging.ContextLoggable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static com.x.scrape.logging.ContextKeys.TASK_ID;
import static com.x.scrape.logging.ContextKeys.URL;
import static com.x.scrape.model.job_definition.configuration.scraping_configuration.DataPointType.IMAGE;

public class Task implements ContextLoggable {
	
	private Long id;
	private Job job;
	
	private URL url;
	private TaskScrapingConfiguration scrapingConfiguration;
	private StorageConfiguration storageConfiguration;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public Job getJob() {
		return job;
	}
	
	public void setJob(final Job job) {
		this.job = job;
	}
	
	public URL getUrl() {
		return url;
	}
	
	public void setUrl(final URL url) {
		this.url = url;
	}
	
	public TaskScrapingConfiguration getScrapingConfiguration() {
		return scrapingConfiguration;
	}
	
	public void setScrapingConfiguration(final TaskScrapingConfiguration scrapingConfiguration) {
		this.scrapingConfiguration = scrapingConfiguration;
	}
	
	public StorageConfiguration getStorageConfiguration() {
		return storageConfiguration;
	}
	
	public void setStorageConfiguration(final StorageConfiguration storageConfiguration) {
		this.storageConfiguration = storageConfiguration;
	}
	
	public List<ImageDownloadTask> getImageDownloadsTask(final Map<String, Object> scrapedData) {
		return scrapingConfiguration.getDataPointConfigurations()
				.stream()
				.filter(dataPointConfiguration -> IMAGE.equals(dataPointConfiguration.getType()))
				.map(imageConfiguration -> createImageDownloadTask(scrapedData, imageConfiguration.getPropertyName()))
				.toList();
	}
	
	private ImageDownloadTask createImageDownloadTask(final Map<String, Object> scrapedData,
	                                                  final String propertyName) {
		
		final String rawUrl = (String) scrapedData.get(propertyName);
		try {
			final URL url = new URL(rawUrl);
			return createImageDownloadTask(url, propertyName);
		} catch (final MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private ImageDownloadTask createImageDownloadTask(final URL url,
	                                                  final String propertyName) {
		final ImageDownloadTask task = new ImageDownloadTask();
		
		task.setUrl(url);
		task.setPropertyName(propertyName);
		
		return task;
	}
	
	@Override
	public Map<String, String> loggingContext() {
		return Map.of(
				TASK_ID, id.toString(),
				URL, url.toString()
		);
	}
}
