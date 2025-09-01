package com.x.scrape.properties;

import com.x.scrape.properties.scraping.ScrapingProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("x-scraper")
public class XScraperProperties {
	
	private List<ScrapingProperties> scraping;
	
	public List<ScrapingProperties> getScraping() {
		return scraping;
	}
	
	public void setScraping(final List<ScrapingProperties> scraping) {
		this.scraping = scraping;
	}
}
