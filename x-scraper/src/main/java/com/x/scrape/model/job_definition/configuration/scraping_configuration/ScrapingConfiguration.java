package com.x.scrape.model.job_definition.configuration.scraping_configuration;

import com.x.scrape.persistence.shared.model.HasId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class ScrapingConfiguration implements HasId {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	private String elementSelector;
	private List<DataPointConfiguration> dataPointConfigurations = new ArrayList<>();
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
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
		this.dataPointConfigurations = dataPointConfigurations;
	}
}
