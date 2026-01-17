package com.x.scrape.persistence.repository;

import com.x.scrape.model.job_definition.JobDefinition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobDefinitionRepository extends CrudRepository<JobDefinition, Long> {
	
	@Override
	List<JobDefinition> findAll();
}
