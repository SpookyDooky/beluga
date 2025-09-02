package com.x.scrape.job;

import com.x.scrape.http.HttpService;
import com.x.scrape.mapper.job.JobMapper;
import com.x.scrape.model.job.Job;
import com.x.scrape.model.job.scraping_configuration.ScrapingConfiguration;
import com.x.scrape.properties.XScraperProperties;
import com.x.scrape.properties.scraping.ScrapingProperties;
import com.x.scrape.scraping.DocumentScrapingService;
import com.x.scrape.storage.StorageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class JobService {
	
	private final Logger logger = LogManager.getLogger();
	
	private final XScraperProperties xScraperProperties;
	private final JobMapper jobMapper;
	
	private final HttpService httpService;
	private final DocumentScrapingService documentScrapingService;
	private final StorageService storageService;
	
	public JobService(final XScraperProperties xScraperProperties,
	                  final JobMapper jobMapper,
	                  final HttpService httpService,
	                  final DocumentScrapingService documentScrapingService,
	                  final StorageService storageService) {
		this.xScraperProperties = xScraperProperties;
		this.jobMapper = jobMapper;
		this.httpService = httpService;
		this.documentScrapingService = documentScrapingService;
		this.storageService = storageService;
	}
	
	@Scheduled(initialDelay = 0L)
	public void startJobs() {
		logger.info("Starting scrape jobs");
		
		xScraperProperties.getJobs()
				.stream()
				.map(jobMapper::map)
				.forEach(this::executeJob);
	}
	
	public void executeJob(final Job job) {
		final List<Map<String, Object>> results = new ArrayList<>();
		
		for (final URL url : job.getUrlConfiguration().getUrls()) {
			final List<Map<String, Object>> scrapedPage = scrapePage(url, job.getScrapingConfiguration());
			logger.info("Finished " + url.toString() + " found " + scrapedPage.size() + " results");
			
			results.addAll(scrapedPage);
			storageService.saveContent(
					job.getStorageConfiguration(),
					results
			);
		}
	}
	
	private List<Map<String, Object>> scrapePage(final URL url,
	                                             final ScrapingConfiguration scrapingConfiguration) {
		final Document document = httpService.retrievePage(url)
				.orElseThrow(() -> new IllegalStateException("Failed to retrieve page " + url.toString() + "."));
		
		return documentScrapingService.scrapeDocument(
				document,
				scrapingConfiguration
		);
	}
}
