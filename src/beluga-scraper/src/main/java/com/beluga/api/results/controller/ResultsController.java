package com.beluga.api.results.controller;

import com.beluga.api.execution.exception.TaskResultNotFoundException;
import com.beluga.api.results.dto.ResultFileInfoDto;
import com.beluga.api.results.dto.TaskResultDto;
import com.beluga.api.results.service.ResultService;
import com.beluga.execution.model.task.Task;
import com.beluga.execution.model.task.TaskExecution;
import com.beluga.logging.CloseableContext;
import com.beluga.logging.ContextLogger;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.model.job_definition.JobExecution;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.beluga.logging.ContextKeys.*;

@RestController
@RequestMapping("/jobs/{jobId}/executions/{executionId}")
public class ResultsController {

    private final ContextLogger logger;
    private final ResultService resultService;

    public ResultsController(final ContextLogger logger,
                             final ResultService resultService) {
        this.logger = logger;
        this.resultService = resultService;
    }

    /**
     * Retrieves the {@link Task}'s.
     *
     * @param jobDefinitionId the id of the {@link JobDefinition} this task belongs to.
     * @param jobExecutionId  the id of the {@link JobExecution} this task belongs to.
     * @param taskExecutionId the id of the {@link TaskExecution} to retrieve the results for.
     * @return the task results.
     */
    @GetMapping("/tasks/{taskId}/results")
    public TaskResultDto getTaskResults(@PathVariable("jobId") final Long jobDefinitionId,
                                        @PathVariable("executionId") final Long jobExecutionId,
                                        @PathVariable("taskId") final Long taskExecutionId) {
        try (final CloseableContext ignored = logger.withKeys(Map.of(
                JOB_ID, jobDefinitionId.toString(),
                JOB_EXECUTION_ID, jobExecutionId.toString(),
                TASK_EXECUTION_ID, taskExecutionId.toString()
        ))) {
            logger.info("Retrieving task results.");
            return resultService.getTaskResult(
                    jobDefinitionId,
                    jobExecutionId,
                    taskExecutionId
            );
        }
    }

    @ExceptionHandler({
            TaskResultNotFoundException.class,
            EntityNotFoundException.class
    })
    public ResponseEntity<Void> handleTaskNotFoundException(final RuntimeException exception) {
        return ResponseEntity.notFound()
                .build();
    }

    @GetMapping("/results")
    public Page<TaskResultDto> getJobExecutionTaskResults(@PathVariable("jobId") final Long jobDefinitionId,
                                                          @PathVariable("executionId") final Long jobExecutionId,
                                                          @RequestParam("page") final int page,
                                                          @RequestParam("size") final int pageSize) {
        try (final CloseableContext ignored = logger.withKeys(Map.of(
                JOB_ID, jobDefinitionId.toString(),
                JOB_EXECUTION_ID, jobExecutionId.toString(),
                PAGE, String.valueOf(page),
                PAGE_SIZE, String.valueOf(pageSize)
        ))) {
            logger.info("Retrieving job execution results.");
            return resultService.getResults(
                    jobDefinitionId,
                    jobExecutionId,
                    page,
                    pageSize
            );
        }
    }

    @GetMapping("/tasks/{taskId}/results/files")
    public List<ResultFileInfoDto> getResultFileInfo(@PathVariable("jobId") final Long jobDefinitionId,
                                                     @PathVariable("executionId") final Long jobExecutionId,
                                                     @PathVariable("taskId") final Long taskExecutionId) {
        try (final CloseableContext ignored = logger.withKeys(Map.of(
                JOB_ID, jobDefinitionId.toString(),
                JOB_EXECUTION_ID, jobExecutionId.toString(),
                TASK_EXECUTION_ID, taskExecutionId.toString()
        ))) {
            logger.info("Retrieving task execution result files.");
            return resultService.getResultFileInfo(
                    jobDefinitionId,
                    jobExecutionId,
                    taskExecutionId
            );
        }
    }

    @GetMapping("/tasks/{taskId}/results/files/download")
    public byte[] downloadFile(@PathVariable("jobId") final Long jobDefinitionId,
                               @PathVariable("executionId") final Long jobExecutionId,
                               @PathVariable("taskId") final Long taskExecutionId,
                               @RequestParam("fileName") final String fileName) {
        try (final CloseableContext ignored = logger.withKeys(Map.of(
                JOB_ID, jobDefinitionId.toString(),
                JOB_EXECUTION_ID, jobExecutionId.toString(),
                TASK_EXECUTION_ID, taskExecutionId.toString(),
                FILE_NAME, fileName
        ))) {
            logger.info("Downloading task result file.");
            return resultService.getResultFileContent(
                    jobDefinitionId,
                    jobExecutionId,
                    taskExecutionId,
                    fileName
            );
        }
    }
}
