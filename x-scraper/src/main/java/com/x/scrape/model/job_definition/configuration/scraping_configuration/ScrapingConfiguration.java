package com.x.scrape.model.job_definition.configuration.scraping_configuration;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class ScrapingConfiguration {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	private String elementSelector;
	
	@OneToMany(
			cascade = ALL,
			mappedBy = "scrapingConfiguration",
			fetch = EAGER,
			orphanRemoval = true
	)
	private List<DataPointConfiguration> dataPointConfigurations = new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public String getElementSelector() {
		return elementSelector;
	}
	
	public void setElementSelector(final String elementSelector) {
		this.elementSelector = elementSelector;
	}
	
	public List<DataPointConfiguration> getDataPointConfigurations() {
		return dataPointConfigurations;
	}
	
	public void setDataPointConfigurations(final List<DataPointConfiguration> dataPointConfigurations) {
		this.dataPointConfigurations.clear();
		this.dataPointConfigurations.addAll(dataPointConfigurations);
		this.dataPointConfigurations.forEach(dataPointConfiguration -> {
			dataPointConfiguration.setScrapingConfiguration(this);
		});
	}
}
