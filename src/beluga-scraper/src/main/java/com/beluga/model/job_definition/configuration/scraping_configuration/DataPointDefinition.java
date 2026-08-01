package com.beluga.model.job_definition.configuration.scraping_configuration;

import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.ExtractionDefinition;
import jakarta.persistence.*;

import static com.beluga.model.job_definition.configuration.scraping_configuration.DataPointType.TEXT;
import static jakarta.persistence.EnumType.STRING;
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
	private String field;
	
	private String attribute;
	
	@Enumerated(STRING)
	private DataPointType type = TEXT;

	@OneToOne
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
	
	public String getField() {
		return field;
	}
	
	public void setField(final String field) {
		this.field = field;
	}
	
	public String getAttribute() {
		return attribute;
	}
	
	public void setAttribute(final String attribute) {
		this.attribute = attribute;
	}
	
	public DataPointType getType() {
		return type;
	}
	
	public void setType(final DataPointType type) {
		this.type = type;
	}

	public ExtractionDefinition getExtractionDefinition() {
		return extractionDefinition;
	}

	public void setExtractionDefinition(ExtractionDefinition extractionDefinition) {
		this.extractionDefinition = extractionDefinition;
	}
}
