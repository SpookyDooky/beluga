package com.beluga.service.task;

import com.beluga.execution.model.task.TaskDefinition;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.persistence.repository.TaskDefinitionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Service
public class TaskDefinitionService {

    private final TaskDefinitionRepository taskDefinitionRepository;

    public TaskDefinitionService(final TaskDefinitionRepository taskDefinitionRepository) {
        this.taskDefinitionRepository = taskDefinitionRepository;
    }

    /**
     * Retrieves a list of all URL's belonging to active {@link TaskDefinition}'s of a {@link JobDefinition}.
     *
     * @param jobDefinitionId the id of the {@link JobDefinition}.
     * @return all URL's.
     */
    @Transactional
    public Set<URL> getActiveUrlsByJobDefinitionId(final Long jobDefinitionId) {
        return taskDefinitionRepository.findAllActiveTaskDefinitionUrlsByJobDefinitionId(jobDefinitionId)
                .stream()
                .map(this::mapToUrl)
                .collect(Collectors.toSet());
    }

    private URL mapToUrl(final String url) {
        try {
            return URI.create(url)
                    .toURL();
        } catch (final MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Retrieves all active {@link TaskDefinition}'s of a {@link JobDefinition}.
     *
     * @param jobDefinitionId the id of the {@link JobDefinition}.
     * @return a list containing all active {@link TaskDefinition}'s of a {@link JobDefinition}.
     */
    @Transactional(propagation = MANDATORY)
    public List<TaskDefinition> getAllActiveByJobDefinitionId(final Long jobDefinitionId) {
        return taskDefinitionRepository.findAllByActiveAndJobDefinitionId(jobDefinitionId);
    }

    /**
     * Retrieves all active URL's for a {@link JobDefinition} where the URL is also in a given list of URL's.
     *
     * @param urls            the given list of URL's
     * @param jobDefinitionId the id of the {@link JobDefinition}.
     * @return the active URL's.
     */
    @Transactional
    public Set<URL> getAllActiveUrlsByJobDefinitionIdAndUrlIn(final Set<URL> urls,
                                                              final Long jobDefinitionId) {
        return taskDefinitionRepository.getAllActiveUrlsByJobDefinitionIdAndInUrls(jobDefinitionId, urls)
                .stream()
                .map(this::mapToUrl)
                .collect(Collectors.toSet());
    }

    /**
     * Sets all {@link TaskDefinition}'s with matching URL's to inactive.
     *
     * @param jobDefinitionId the id of the {@link JobDefinition}.
     * @param urls            the URL's for which to set {@link TaskDefinition}'s to inactive.
     */
    @Transactional
    public void setAllToInactiveByJobDefinitionIdAndUrlNotInUrls(final Long jobDefinitionId,
                                                                 final Set<URL> urls) {
        taskDefinitionRepository.setActiveFalseByJobDefinitionIdAndUrlNotInUrls(
                jobDefinitionId,
                urls
        );
    }
}
