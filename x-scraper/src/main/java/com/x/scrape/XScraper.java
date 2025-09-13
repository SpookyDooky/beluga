package com.x.scrape;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class XScraper {
	
	public static void main(final String... args) {
		SpringApplication.run(XScraper.class);
	}
}
