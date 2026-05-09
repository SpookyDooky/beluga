package com.beluga.model.job_definition.configuration.scraping_configuration;

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
public class ScrapingDefinition {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	private String elementSelector;
	
	@OneToMany(
			cascade = ALL,
			mappedBy = "scrapingDefinition",
			fetch = EAGER,
			orphanRemoval = true
	)
	private List<DataPointDefinition> dataPointDefinitions = new ArrayList<>();
	
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
	
	public List<DataPointDefinition> getDataPointDefinitions() {
		return dataPointDefinitions;
	}
	
	public void setDataPointDefinitions(final List<DataPointDefinition> dataPointDefinitions) {
		this.dataPointDefinitions.clear();
		this.dataPointDefinitions.addAll(dataPointDefinitions);
		this.dataPointDefinitions.forEach(dataPointConfiguration -> {
			dataPointConfiguration.setScrapingDefinition(this);
		});
	}
}
