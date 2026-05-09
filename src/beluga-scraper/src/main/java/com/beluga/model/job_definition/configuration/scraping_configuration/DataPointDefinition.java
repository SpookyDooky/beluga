package com.beluga.model.job_definition.configuration.scraping_configuration;

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
	private String propertyName;
	
	private String attribute;
	
	@Enumerated(STRING)
	private DataPointType type = TEXT;
	
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
	
	public String getPropertyName() {
		return propertyName;
	}
	
	public void setPropertyName(final String propertyName) {
		this.propertyName = propertyName;
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
}
