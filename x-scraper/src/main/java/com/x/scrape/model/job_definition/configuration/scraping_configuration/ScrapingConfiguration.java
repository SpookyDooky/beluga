package com.x.scrape.model.job_definition.configuration.scraping_configuration;

import com.x.scrape.persistence.shared.model.HasId;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class ScrapingConfiguration implements HasId {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	private String elementSelector;
	
	@OneToMany(
			cascade = ALL,
			mappedBy = "scrapingConfiguration",
			orphanRemoval = true,
			fetch = EAGER
	)
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
		dataPointConfigurations.forEach(dataPointConfiguration -> {
			dataPointConfiguration.setScrapingConfiguration(this);
		});
	}
}
