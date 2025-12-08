package com.x.scrape.mapper.task;

import com.x.scrape.model.task.TaskDefinition;
import com.x.scrape.properties.scraping.url.UrlProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskDefinitionMapperService {
	
	public List<TaskDefinition> map(final UrlProperties urlProperties) {
		if (!urlProperties.getUrls().isEmpty()) {
			return map(urlProperties.getUrls());
		}
		
		return map(readUrlFile(urlProperties.getUrlFile()));
	}
	
	public List<TaskDefinition> map(final Collection<URL> urls) {
		return urls.stream()
				.map(this::createTaskDefinition)
				.collect(Collectors.toList());
	}
	
	private TaskDefinition createTaskDefinition(final URL url) {
		final TaskDefinition taskDefinition = new TaskDefinition();
		taskDefinition.setUrl(url);
		return taskDefinition;
	}
	
	private List<URL> readUrlFile(final String urlFile) {
		try {
			return Files.readAllLines(Path.of(urlFile))
					.stream()
					.map(this::createUrl)
					.toList();
		} catch (final IOException e) {
			throw new IllegalStateException("Unexpected exception occurred whilst reading url file.", e);
		}
	}
	
	private URL createUrl(final String url) {
		try {
			return URI.create(url).toURL();
		} catch (final MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
