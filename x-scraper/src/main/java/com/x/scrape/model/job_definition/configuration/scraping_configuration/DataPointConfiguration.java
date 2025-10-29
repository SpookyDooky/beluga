package com.x.scrape.model.job_definition.configuration.scraping_configuration;

import com.x.scrape.persistence.shared.model.HasId;
import jakarta.persistence.*;

import static com.x.scrape.model.job_definition.configuration.scraping_configuration.DataPointType.TEXT;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class DataPointConfiguration implements HasId {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="scraping_configuration_id")
	private ScrapingConfiguration scrapingConfiguration;
	
	private String selector;
	private String propertyName;
	
	private String attribute;
	
	@Enumerated(STRING)
	private DataPointType type = TEXT;
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(final Long id) {
		this.id = id;
	}
	
	public ScrapingConfiguration getScrapingConfiguration() {
		return scrapingConfiguration;
	}
	
	public void setScrapingConfiguration(final ScrapingConfiguration scrapingConfiguration) {
		this.scrapingConfiguration = scrapingConfiguration;
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
