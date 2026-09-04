package com.beluga.service.results;

import com.beluga.execution.model.task.TaskExecution;
import com.beluga.model.job_definition.JobExecution;
import com.beluga.model.result.ResultFile;
import com.beluga.persistence.repository.ResultFileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

/**
 * Service for finding {@link ResultFile}'s.
 */
@Service
public class ResultFileService {

    private final ResultFileRepository resultFileRepository;

    public ResultFileService(final ResultFileRepository resultFileRepository) {
        this.resultFileRepository = resultFileRepository;
    }

    /**
     * Find a specific {@link ResultFile}.
     *
     * @param taskExecution the {@link TaskExecution} it belongs to.
     * @param key           the key of the {@link ResultFile}
     * @return {@link Optional} containing the file, otherwise empty.
     */
    @Transactional(propagation = MANDATORY)
    public Optional<ResultFile> findByTaskExecutionAndKey(final TaskExecution taskExecution,
                                                          final String key) {
        return resultFileRepository.findByTaskExecutionAndKey(taskExecution, key);
    }

    /**
     * Finds all {@link ResultFile}'s belong to a certain {@link JobExecution}.
     *
     * @param jobExecution the {@link JobExecution} it belongs to.
     * @param key          the key of the {@link ResultFile}.
     * @param pageable     pageable
     * @return page containing {@link ResultFile}'s
     */
    @Transactional(propagation = MANDATORY)
    public Page<ResultFile> findByJobExecutionAndKeyPaged(final JobExecution jobExecution,
                                                          final String key,
                                                          final Pageable pageable) {
        return resultFileRepository.findByTaskExecution_JobExecutionAndKey(jobExecution, key, pageable);
    }
}
