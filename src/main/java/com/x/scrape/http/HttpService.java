package com.x.scrape.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Optional;

@Service
public class HttpService {
	
	private final Logger logger = LogManager.getLogger();
	
	public Optional<Document> retrievePage(final URL url) {
		logger.info("Retrieving document for: " + url);
		
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
}
