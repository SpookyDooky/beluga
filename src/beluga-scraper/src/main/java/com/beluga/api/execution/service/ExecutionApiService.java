package com.beluga.api.execution.service;

import com.beluga.api.execution.dto.ReadJobExecutionDto;
import com.beluga.api.execution.dto.ReadJobExecutionWithTasksDto;
import com.beluga.api.execution.mapper.ReadJobExecutionMapper;
import com.beluga.execution.model.job.Job;
import com.beluga.execution.service.job.JobExecutionService;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.model.job_definition.JobExecution;
import com.beluga.service.job.JobDefinitionService;
import com.beluga.service.job.JobService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Optional;

import static com.beluga.model.job_definition.JobStatus.*;

// Todo - this entire service should support multiple running jobs and executions should be stopped/resumed/paused based on the job execution id.
@Service
public class ExecutionApiService {

    private final JobDefinitionService jobDefinitionService;
    private final JobService jobService;
    private final JobExecutionService jobExecutionService;
    private final ReadJobExecutionMapper readJobExecutionMapper;

    public ExecutionApiService(final JobDefinitionService jobDefinitionService,
                               final JobService jobService,
                               final JobExecutionService jobExecutionService,
                               final ReadJobExecutionMapper readJobExecutionMapper) {
        this.jobDefinitionService = jobDefinitionService;
        this.jobService = jobService;
        this.jobExecutionService = jobExecutionService;
        this.readJobExecutionMapper = readJobExecutionMapper;
    }

    /**
     * Starts a job, after the method is finished it is guaranteed a job has started.
     *
     * @param jobDefinitionId id of the {@link Job}'s {@link JobDefinition} to start.
     */
    @Transactional
    public void start(final Long jobDefinitionId) {
        final Job job = jobService.createJobByJobDefinitionId(jobDefinitionId);

        start(job);
    }

    private void start(final Job job) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                jobExecutionService.executeJob(job);
            }
        });
    }

    /**
     * Stops a job, after the method is finished it is guaranteed a job is stopped.
     *
     * @param jobDefinitionId id of the {@link Job}'s {@link JobDefinition} to stop.
     */
    @Transactional
    public void stop(final Long jobDefinitionId) {
        final JobDefinition jobDefinition = jobDefinitionService.getById(jobDefinitionId);
        final Optional<JobExecution> latestExecutionOptional = jobDefinition.getMostRecentExecution();

        if (latestExecutionOptional.isPresent()) {
            final JobExecution latestExecution = latestExecutionOptional.get();

            // Todo - reconsider when a job can be stopped
            if (latestExecution.getStatus() == COMPLETED) {
                return;
            }

            jobDefinitionService.setJobExecutionStatusById(STOPPED, jobDefinitionId, latestExecution.getId());
            jobExecutionService.stop(latestExecution.getId());
        }
    }

    /**
     * Pauses a job, after the method is finished it is guaranteed a job is paused.
     *
     * @param jobDefinitionId id of the {@link Job}'s {@link JobDefinition} to pause.
     */
    @Transactional
    public void pause(final Long jobDefinitionId) {
        final JobDefinition jobDefinition = jobDefinitionService.getById(jobDefinitionId);
        final Optional<JobExecution> latestExecutionOptional = jobDefinition.getMostRecentExecution();

        if (latestExecutionOptional.isPresent()) {
            final JobExecution latestExecution = latestExecutionOptional.get();

            // Todo - reconsider when a job can be paused
            if (latestExecution.getStatus() == COMPLETED) {
                return;
            }

            jobDefinitionService.setJobExecutionStatusById(PAUSED, jobDefinitionId, latestExecution.getId());
            jobExecutionService.pause(latestExecution.getId());
        }
    }

    /**
     * Resumes a job, after the method is finished it is guaranteed a job is no longer paused.
     *
     * @param jobDefinitionId id of the {@link Job}'s {@link JobDefinition} to resume.
     */
    @Transactional
    public void resume(final Long jobDefinitionId) {
        final JobDefinition jobDefinition = jobDefinitionService.getById(jobDefinitionId);
        final Optional<JobExecution> latestExecutionOptional = jobDefinition.getMostRecentExecution();

        if (latestExecutionOptional.isPresent()) {
            final JobExecution latestExecution = latestExecutionOptional.get();

            if (latestExecution.getStatus() != PAUSED) {
                return;
            }

            jobDefinitionService.setJobExecutionStatusById(ACTIVE, jobDefinitionId, latestExecution.getId());
            final Optional<Job> jobOptional = jobService.createResumedJob(jobDefinitionId);

            if (jobOptional.isPresent()) {
                final Job job = jobOptional.get();
                jobExecutionService.executeJob(job);
            }
        }
    }

    /**
     * Finds the latest {@link JobExecution} for a {@link JobDefinition}.
     *
     * @param jobDefinitionId id of the {@link JobDefinition} to retrieve the latest {@link JobExecution} for.
     * @return the latest {@link JobExecution}, empty if there are no {@link JobExecution}'s
     */
    @Transactional
    public Optional<ReadJobExecutionDto> getLatestJobExecution(final Long jobDefinitionId) {
        final JobDefinition jobDefinition = jobDefinitionService.getById(jobDefinitionId);

        return jobDefinition.getMostRecentExecution()
                .map(readJobExecutionMapper::map);
    }

    /**
     * Finds the all {@link JobExecution}'s for a {@link JobDefinition}.
     *
     * @param jobDefinitionId id of the {@link JobDefinition} to retrieve the latest {@link JobExecution} for.
     * @return all {@link JobExecution}'s for a {@link JobDefinition}.
     */
    @Transactional
    public List<ReadJobExecutionDto> getExecutions(final Long jobDefinitionId) {
        final JobDefinition jobDefinition = jobDefinitionService.getById(jobDefinitionId);

        return jobDefinition.getExecutions().stream()
                .map(readJobExecutionMapper::map)
                .toList();
    }

    /**
     * Returns a specific {@link JobExecution} of a {@link JobDefinition} with task information.
     *
     * @param jobDefinitionId id of the {@link JobDefinition}.
     * @param jobExecutionId  id of the {@link JobExecution}
     * @return empty if execution does not exist.
     */
    @Transactional
    public Optional<ReadJobExecutionWithTasksDto> getExecution(final Long jobDefinitionId,
                                                               final Long jobExecutionId) {
        final JobDefinition jobDefinition = jobDefinitionService.getById(jobDefinitionId);

        return jobDefinition.findExecutionById(jobExecutionId)
                .map(readJobExecutionMapper::mapWithTasks);
    }
}
