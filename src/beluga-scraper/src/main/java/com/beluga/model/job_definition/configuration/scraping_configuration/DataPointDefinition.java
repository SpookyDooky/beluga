package com.beluga.model.job_definition.configuration.scraping_configuration;

import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.ExtractionDefinition;
import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class DataPointDefinition {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "scraping_definition_id")
	private ScrapingDefinition scrapingDefinition;
	
	private String selector;

	@OneToOne
	@JoinColumn(name = "extraction_definition_id")
	private ExtractionDefinition extractionDefinition;

	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public ScrapingDefinition getScrapingDefinition() {
		return scrapingDefinition;
	}
	
	public void setScrapingDefinition(final ScrapingDefinition scrapingDefinition) {
		this.scrapingDefinition = scrapingDefinition;
	}
	
	public String getSelector() {
		return selector;
	}
	
	public void setSelector(final String selector) {
		this.selector = selector;
	}

	public ExtractionDefinition getExtractionDefinition() {
		return extractionDefinition;
	}

	public void setExtractionDefinition(ExtractionDefinition extractionDefinition) {
		this.extractionDefinition = extractionDefinition;
	}
}
