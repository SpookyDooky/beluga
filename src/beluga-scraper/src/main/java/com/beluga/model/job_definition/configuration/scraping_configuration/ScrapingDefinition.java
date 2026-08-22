package com.beluga.model.job_definition.configuration.scraping_configuration;

import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.ExtractionDefinition;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class ScrapingDefinition {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	private String itemSelector;
	
	@OneToMany(
			cascade = ALL,
			mappedBy = "scrapingDefinition",
			orphanRemoval = true
	)
	private final List<ExtractionDefinition> extractionDefinitions = new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public String getItemSelector() {
		return itemSelector;
	}
	
	public void setItemSelector(final String itemSelector) {
		this.itemSelector = itemSelector;
	}
	
	public List<ExtractionDefinition> getExtractionDefinitions() {
		return extractionDefinitions;
	}
	
	public void setExtractionDefinitions(final List<ExtractionDefinition> extractionDefinitions) {
		this.extractionDefinitions.clear();
		this.extractionDefinitions.addAll(extractionDefinitions);
		this.extractionDefinitions.forEach(extractionDefinition -> {
			extractionDefinition.setScrapingDefinition(this);
		});
	}
}
