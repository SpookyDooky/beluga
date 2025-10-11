package com.x.scrape.http;

import com.x.scrape.activity_logging.event.ActivityEvent;
import com.x.scrape.activity_logging.model.RequestActivity;
import com.x.scrape.logging.ContextLogger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

@Service
public class HttpService {
	
	private final ContextLogger logger;
	private final ApplicationEventPublisher applicationEventPublisher;
	
	public HttpService(final ContextLogger logger,
	                   final ApplicationEventPublisher applicationEventPublisher) {
		this.logger = logger;
		this.applicationEventPublisher = applicationEventPublisher;
	}
	
	public Optional<Document> retrievePageAsDocument(final URL url) {
		logger.info("Retrieving document for: " + url);
		applicationEventPublisher.publishEvent(new ActivityEvent(new RequestActivity(url)));
		
		try {
			
			return Optional.of(
					Jsoup.connect(url.toString())
							.timeout(10_000)
							.get()
			);
		} catch (final Exception e) {
			logger.error("Failed to retrieve document for: " + url, e);
			return Optional.empty();
		}
	}
	
	public InputStream get(final URL url) {
		try {
			applicationEventPublisher.publishEvent(new ActivityEvent(new RequestActivity(url)));
			return new BufferedInputStream(url.openStream());
		} catch (final IOException e) {
			throw new IllegalStateException("Could not get URL", e);
		}
	}
}
