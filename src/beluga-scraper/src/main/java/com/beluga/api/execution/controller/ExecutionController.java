package com.beluga.api.execution.controller;

import com.beluga.api.execution.dto.ReadJobExecutionDto;
import com.beluga.api.execution.dto.ReadJobExecutionWithTasksDto;
import com.beluga.api.execution.service.ExecutionApiService;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.model.job_definition.JobExecution;
import com.beluga.service.exception.JobDefinitionNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs/{jobDefinitionId}")
public class ExecutionController {

    private final ExecutionApiService executionApiService;

    public ExecutionController(final ExecutionApiService executionApiService) {
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
        executionApiService.start(jobDefinitionId);

        return ResponseEntity.noContent()
                .build();
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
        executionApiService.stop(jobDefinitionId);

        return ResponseEntity.noContent()
                .build();
    }

    /**
     * Pauses the execution of a {@link JobDefinition}.
     *
     * @param jobDefinitionId the id of the {@link JobDefinition}
     * @return {@link ResponseEntity#noContent()}
     */
    @PostMapping("/pause")
    public ResponseEntity<Void> pause(@PathVariable("jobDefinitionId") final Long jobDefinitionId) {
        executionApiService.pause(jobDefinitionId);

        return ResponseEntity.noContent()
                .build();
    }

    /**
     * Resumes the execution of a {@link JobDefinition}
     *
     * @param jobDefinitionId the id of the {@link JobDefinition}
     * @return {@link ResponseEntity#noContent()}
     */
    @PostMapping("/resume")
    public ResponseEntity<Void> resume(@PathVariable("jobDefinitionId") final Long jobDefinitionId) {
        executionApiService.resume(jobDefinitionId);

        return ResponseEntity.noContent()
                .build();
    }

    /**
     * Retrieves the latest execution of a specific {@link JobDefinition}.
     *
     * @param jobDefinitionId the id of the {@link JobDefinition}
     * @return the latest execution.
     */
    @GetMapping("/executions/latest")
    public ResponseEntity<ReadJobExecutionDto> getLatestExecution(@PathVariable("jobDefinitionId") final Long jobDefinitionId) {
        return ResponseEntity.of(executionApiService.getLatestJobExecution(jobDefinitionId));
    }

    /**
     * Retrieves all executions of a {@link JobDefinition}
     *
     * @param jobDefinitionId the id of the {@link JobDefinition}
     * @return a list containing all executions of the job
     */
    @GetMapping("/executions")
    public List<ReadJobExecutionDto> getExecutions(@PathVariable("jobDefinitionId") final Long jobDefinitionId) {
        return executionApiService.getExecutions(jobDefinitionId);
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
        return ResponseEntity.of(executionApiService.getExecution(jobDefinitionId, executionId));
    }
}
