package com.beluga.api.execution.controller;

import com.beluga.api.execution.dto.ReadJobExecutionDto;
import com.beluga.api.execution.dto.ReadJobExecutionWithTasksDto;
import com.beluga.api.execution.service.ExecutionApiService;
import com.beluga.logging.CloseableContext;
import com.beluga.logging.ContextLogger;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.model.job_definition.JobExecution;
import com.beluga.service.exception.JobDefinitionNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.beluga.logging.ContextKeys.JOB_EXECUTION_ID;
import static com.beluga.logging.ContextKeys.JOB_ID;

@RestController
@RequestMapping("/jobs/{jobDefinitionId}")
public class ExecutionController {

    private final ContextLogger logger;
    private final ExecutionApiService executionApiService;

    public ExecutionController(final ContextLogger logger,
                               final ExecutionApiService executionApiService) {
        this.logger = logger;
        this.executionApiService = executionApiService;
    }

    /**
     * Starts the execution of a {@link JobDefinition}
     *
     * @param jobDefinitionId the id of the {@link JobDefinition}
     * @return {@link ResponseEntity#noContent()}
     */
    @PostMapping("/start")
    public ResponseEntity<Void> start(@PathVariable("jobDefinitionId") final Long jobDefinitionId) {
        try (final CloseableContext ignored = logger.withKey(JOB_ID, jobDefinitionId.toString())) {
            logger.info("Starting job execution.");

            executionApiService.start(jobDefinitionId);

            return ResponseEntity.noContent()
                    .build();
        }
    }

    @ExceptionHandler(JobDefinitionNotFoundException.class)
    public ResponseEntity<Void> handle() {
        return ResponseEntity.notFound()
                .build();
    }

    /**
     * Stops the execution of a {@link JobDefinition}
     *
     * @param jobDefinitionId the id of the {@link JobDefinition}
     * @return {@link ResponseEntity#noContent()}
     */
    @PostMapping("/stop")
    public ResponseEntity<Void> stop(@PathVariable("jobDefinitionId") final Long jobDefinitionId) {
        try (final CloseableContext ignored = logger.withKey(JOB_ID, jobDefinitionId.toString())) {
            logger.info("Stopping job execution.");
            executionApiService.stop(jobDefinitionId);

            return ResponseEntity.noContent()
                    .build();
        }
    }

    /**
     * Pauses the execution of a {@link JobDefinition}.
     *
     * @param jobDefinitionId the id of the {@link JobDefinition}
     * @return {@link ResponseEntity#noContent()}
     */
    @PostMapping("/pause")
    public ResponseEntity<Void> pause(@PathVariable("jobDefinitionId") final Long jobDefinitionId) {
        try (final CloseableContext ignored = logger.withKey(JOB_ID, jobDefinitionId.toString())) {
            logger.info("Pausing job execution.");
            executionApiService.pause(jobDefinitionId);

            return ResponseEntity.noContent()
                    .build();
        }
    }

    /**
     * Resumes the execution of a {@link JobDefinition}
     *
     * @param jobDefinitionId the id of the {@link JobDefinition}
     * @return {@link ResponseEntity#noContent()}
     */
    @PostMapping("/resume")
    public ResponseEntity<Void> resume(@PathVariable("jobDefinitionId") final Long jobDefinitionId) {
        try (final CloseableContext ignored = logger.withKey(JOB_ID, jobDefinitionId.toString())) {
            logger.info("Resuming job execution.");
            executionApiService.resume(jobDefinitionId);

            return ResponseEntity.noContent()
                    .build();
        }
    }

    /**
     * Retrieves the latest execution of a specific {@link JobDefinition}.
     *
     * @param jobDefinitionId the id of the {@link JobDefinition}
     * @return the latest execution.
     */
    @GetMapping("/executions/latest")
    public ResponseEntity<ReadJobExecutionDto> getLatestExecution(@PathVariable("jobDefinitionId") final Long jobDefinitionId) {
        try (final CloseableContext ignored = logger.withKey(JOB_ID, jobDefinitionId.toString())) {
            logger.info("Retrieving latest job execution.");
            return ResponseEntity.of(executionApiService.getLatestJobExecution(jobDefinitionId));
        }
    }

    /**
     * Retrieves all executions of a {@link JobDefinition}
     *
     * @param jobDefinitionId the id of the {@link JobDefinition}
     * @return a list containing all executions of the job
     */
    @GetMapping("/executions")
    public List<ReadJobExecutionDto> getExecutions(@PathVariable("jobDefinitionId") final Long jobDefinitionId) {
        try (final CloseableContext ignored = logger.withKey(JOB_ID, jobDefinitionId.toString())) {
            logger.info("Retrieving all job executions.");
            return executionApiService.getExecutions(jobDefinitionId);
        }
    }

    /**
     * Retrieves an execution of a {@link JobDefinition}.
     *
     * @param jobDefinitionId the id of the {@link JobDefinition}
     * @param executionId     the id of the {@link JobExecution}
     * @return {@link ResponseEntity#noContent()}
     */
    @GetMapping("/executions/{executionId}")
    public ResponseEntity<ReadJobExecutionWithTasksDto> getExecution(@PathVariable("jobDefinitionId") final Long jobDefinitionId,
                                                                     @PathVariable("executionId") final Long executionId) {
        try (final CloseableContext ignored = logger.withKeys(Map.of(
                JOB_ID, jobDefinitionId.toString(),
                JOB_EXECUTION_ID, executionId.toString()
        ))) {
            logger.info("Retrieving job execution.");
            return ResponseEntity.of(executionApiService.getExecution(jobDefinitionId, executionId));
        }
    }
}
