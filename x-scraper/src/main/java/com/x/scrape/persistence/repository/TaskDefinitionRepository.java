package com.x.scrape.persistence.repository;

import com.x.scrape.execution.model.task.TaskDefinition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface TaskDefinitionRepository extends CrudRepository<TaskDefinition, Long> {
	
	@Query(
			nativeQuery = true,
			value = "select url from task_definition where active and job_definition_id = :jobDefinitionId"
	)
	Set<String> findAllActiveTaskDefinitionUrlsByJobDefinitionId(@Param("jobDefinitionId") Long jobDefinitionId);
	
	@Query(
			nativeQuery = true,
			value = "select * from task_definition where active and job_definition_id = :jobDefinitionId"
	)
	List<TaskDefinition> findAllByActiveAndJobDefinitionId(@Param("jobDefinitionId") Long jobDefinitionId);
}
