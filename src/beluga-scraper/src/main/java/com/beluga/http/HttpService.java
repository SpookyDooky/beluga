package com.beluga.http;

import com.beluga.activity_logging.activitiy.RequestActivity;
import com.beluga.activity_logging.event.ActivityEvent;
import com.beluga.logging.ContextLogger;
import com.beluga.util.TimingService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

@Service
public class HttpService {
	
	private final ContextLogger logger;
	private final ApplicationEventPublisher applicationEventPublisher;
	private final TimingService timingService;
	
	public HttpService(final ContextLogger logger,
	                   final ApplicationEventPublisher applicationEventPublisher,
	                   final TimingService timingService) {
		this.logger = logger;
		this.applicationEventPublisher = applicationEventPublisher;
		this.timingService = timingService;
	}
	
	public Optional<Document> retrievePageAsDocument(final URL url) {
		logger.info("Retrieving document for: " + url);
		final UUID timingUuid = timingService.start();
		
		try {
			final Optional<Document> document =  Optional.of(
					Jsoup.connect(url.toString())
							.timeout(30_000)
							.get()
			);
			
			applicationEventPublisher.publishEvent(new ActivityEvent(new RequestActivity(url, timingService.stop(timingUuid), true)));
			return document;
		} catch (final Exception e) {
			logger.error("Failed to retrieve document for: " + url, e);
			applicationEventPublisher.publishEvent(new ActivityEvent(new RequestActivity(url, timingService.stop(timingUuid), false)));
			return Optional.empty();
		}
	}
	
	public InputStream get(final URL url) {
		final UUID timingUuid = timingService.start();
		
		try {
			final InputStream inputStream =  new BufferedInputStream(url.openStream());
			applicationEventPublisher.publishEvent(new ActivityEvent(new RequestActivity(url, timingService.stop(timingUuid), true)));
			
			return inputStream;
		} catch (final IOException e) {
			applicationEventPublisher.publishEvent(new ActivityEvent(new RequestActivity(url, timingService.stop(timingUuid), false)));
			throw new IllegalStateException("Could not get URL", e);
		}
	}
}
