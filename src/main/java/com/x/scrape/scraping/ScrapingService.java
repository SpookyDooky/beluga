package com.x.scrape.scraping;

import com.x.scrape.http.HttpService;
import com.x.scrape.properties.XScraperProperties;
import com.x.scrape.properties.scraping.ScrapingProperties;
import com.x.scrape.properties.scraping.JobProperties;
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
public class ScrapingService {
	
	private final Logger logger = LogManager.getLogger();
	
	private final List<JobProperties> jobProperties;
	
	private final HttpService httpService;
	private final DocumentScrapingService documentScrapingService;
	private final StorageService storageService;
	
	public ScrapingService(final XScraperProperties xScraperProperties,
						   final HttpService httpService,
	                       final DocumentScrapingService documentScrapingService,
	                       final StorageService storageService) {
		this.jobProperties = xScraperProperties.getJobs();
		this.httpService = httpService;
		this.documentScrapingService = documentScrapingService;
		this.storageService = storageService;
	}
	
	/**
	 * Should not be an event-listener, due to the fact that spring does not like unhandled exceptions in event listeners that deal
	 * with application state events.
	 */
	@Scheduled(initialDelay = 1L)
	public void startScraping() {
		jobProperties.forEach(this::scrape);
	}
	
	private void scrape(final JobProperties jobProperties) {
		logger.info("Start scraping.");
		
		final List<Map<String, Object>> results = new ArrayList<>();
		
		for (final URL url : jobProperties.getUrl().getUrls()) {
			final List<Map<String, Object>> scrapedPage = scrapePage(url, jobProperties.getScraping());
			logger.info("Finished " + url.toString() + " found " + scrapedPage.size() + " results");
			
			results.addAll(scrapedPage);
			storageService.saveContent(
					jobProperties.getStorage(),
					results
			);
		}
	}
	
	private List<Map<String, Object>> scrapePage(final URL url,
	                                             final ScrapingProperties dataScrapingProperties) {
		final Document document = httpService.retrievePage(url)
				.orElseThrow(() -> new IllegalStateException("Failed to retrieve page " + url.toString() + "."));
		
		return documentScrapingService.scrapeDocument(
				document,
				dataScrapingProperties
		);
	}
}
