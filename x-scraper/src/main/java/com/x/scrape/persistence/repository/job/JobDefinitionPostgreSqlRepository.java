package com.x.scrape.persistence.repository.job;

import com.x.scrape.model.job_definition.JobDefinition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDefinitionPostgreSqlRepository extends JobDefinitionRepository, CrudRepository<JobDefinition, Long> {

}
