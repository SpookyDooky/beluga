package com.beluga.service.job;

import com.beluga.execution.model.task.TaskDefinition;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.model.job_definition.JobExecution;
import com.beluga.model.job_definition.JobStatus;
import com.beluga.persistence.repository.JobDefinitionRepository;
import com.beluga.service.exception.JobDefinitionNotFoundException;
import com.beluga.service.task.TaskDefinitionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.function.Predicate.not;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Service
public class JobDefinitionService {

    private final JobDefinitionRepository repository;
    private final TaskDefinitionService taskDefinitionService;

    public JobDefinitionService(final JobDefinitionRepository repository,
                                final TaskDefinitionService taskDefinitionService) {
        this.repository = repository;
        this.taskDefinitionService = taskDefinitionService;
    }

    /**
     * Saves a {@link JobDefinition}
     *
     * @param jobDefinition the {@link JobDefinition} to save.
     * @return the saved {@link JobDefinition}.
     */
    @Transactional
    public JobDefinition save(final JobDefinition jobDefinition) {
        return repository.save(jobDefinition);
    }

    /**
     * Retrieves a {@link JobDefinition} by its id.
     *
     * @param id the id of the {@link JobDefinition}.
     * @return the {@link JobDefinition}.
     * @throws JobDefinitionNotFoundException in case no {@link JobDefinition} exists with the id.
     */
    @Transactional(propagation = MANDATORY)
    public JobDefinition getById(final Long id) {
        return repository.findById(id)
                .orElseThrow(JobDefinitionNotFoundException::new);
    }

    /**
     * Retrieves a {@link JobDefinition} by its id if it exists.
     *
     * @param id the id of the {@link JobDefinition}.
     * @return the {@link JobDefinition} if it exists or an empty {@link Optional}
     */
    @Transactional(propagation = MANDATORY)
    public Optional<JobDefinition> findById(final Long id) {
        return repository.findById(id);
    }

    @Transactional
    public void setTaskDefinitionsInactiveByUrl(final Long id,
                                                final Collection<URL> urls) {
        final JobDefinition jobDefinition = getById(id);
        jobDefinition.setTaskDefinitionsInactiveByUrl(urls);

        repository.save(jobDefinition);
    }

    /**
     * Sets the status of a {@link JobExecution}.
     *
     * @param status          the status to set.
     * @param jobDefinitionId the {@link JobDefinition} the {@link JobExecution} belongs to.
     * @param jobExecutionId  the id of the {@link JobExecution}.
     */
    @Transactional
    public void setJobExecutionStatusById(final JobStatus status,
                                          final Long jobDefinitionId,
                                          final Long jobExecutionId) {
        final JobDefinition jobDefinition = getById(jobDefinitionId);

        final JobExecution jobExecution = jobDefinition.getExecutionById(jobExecutionId);
        jobExecution.setStatus(status);

        repository.save(jobDefinition);
    }

    @Transactional
    public void addTaskDefinitionsById(final List<TaskDefinition> taskDefinitions,
                                       final Long jobDefinitionId) {
        final JobDefinition jobDefinition = getById(jobDefinitionId);

        final Set<URL> activeTaskDefinitionUrls = taskDefinitionService.getActiveUrlsByJobDefinitionId(jobDefinitionId);
        jobDefinition.addTaskDefinitions(
                taskDefinitions.stream()
                        .filter(not(taskDefinition -> activeTaskDefinitionUrls.contains(taskDefinition.getUrl())))
                        .toList()
        );

        save(jobDefinition);
    }

    /**
     * Returns all active {@link TaskDefinition}'s belonging to a {@link JobDefinition}.
     *
     * @param jobDefinitionId the id of the {@link JobDefinition} to retrieve all active {@link TaskDefinition}'s for.
     * @return all active {@link TaskDefinition}'s
     */
    @Transactional
    public List<TaskDefinition> getActiveTaskDefinitionsById(final Long jobDefinitionId) {
        return taskDefinitionService.getAllActiveByJobDefinitionId(jobDefinitionId);
    }
}
