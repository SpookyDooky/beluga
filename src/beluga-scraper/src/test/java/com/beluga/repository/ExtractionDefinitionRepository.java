package com.beluga.repository;

import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.ExtractionDefinition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtractionDefinitionRepository extends CrudRepository<ExtractionDefinition, Long> {
}
