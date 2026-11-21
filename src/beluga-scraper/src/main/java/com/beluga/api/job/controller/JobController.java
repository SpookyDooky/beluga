package com.beluga.api.job.controller;

import com.beluga.api.job.dto.read.ReadJobDefinitionDto;
import com.beluga.api.job.dto.write.WriteJobDefinitionDto;
import com.beluga.api.job.validation.validator.JobNameValidator;
import com.beluga.logging.CloseableContext;
import com.beluga.logging.ContextLogger;
import com.beluga.mapper.job.JobDefinitionMapper;
import com.beluga.model.job_definition.JobDefinition;
import com.beluga.service.exception.JobDefinitionNotFoundException;
import com.beluga.service.job.JobDefinitionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static com.beluga.logging.ContextKeys.JOB_ID;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final ContextLogger logger;
    private final JobNameValidator jobNameValidator;
    private final JobDefinitionMapper jobDefinitionMapper;
    private final JobDefinitionService jobDefinitionService;

    public JobController(final ContextLogger logger,
                         final JobNameValidator jobNameValidator,
                         final JobDefinitionMapper jobDefinitionMapper,
                         final JobDefinitionService jobDefinitionService) {
        this.logger = logger;
        this.jobNameValidator = jobNameValidator;
        this.jobDefinitionMapper = jobDefinitionMapper;
        this.jobDefinitionService = jobDefinitionService;
    }

    /**
     * Create a new {@link JobDefinition}.
     */
    @PostMapping
    @Transactional
    public ReadJobDefinitionDto create(@RequestBody @Valid final WriteJobDefinitionDto job) {
        logger.info("Creating new job.");

        jobNameValidator.validate(job.getName());

        final JobDefinition jobDefinition = jobDefinitionMapper.map(job);
        jobDefinitionService.save(jobDefinition);

        return jobDefinitionMapper.map(jobDefinition);
    }

    @ExceptionHandler({
            JobDefinitionNotFoundException.class
    })
    public ResponseEntity<Void> handleJobDefinitionNotFoundException(final RuntimeException exception) {
        return ResponseEntity.notFound()
                .build();
    }

    /**
     * Retrieves a {@link JobDefinition} by its id.
     *
     * @param id id of the {@link JobDefinition}.
     * @return the {@link JobDefinition}.
     */
    @GetMapping("/{id}")
    @Transactional
    public ReadJobDefinitionDto get(@PathVariable("id") final Long id) {
        try (final CloseableContext ignored = logger.with(JOB_ID, id.toString())) {
            logger.info("Retrieving job.");

            return jobDefinitionMapper.map(jobDefinitionService.getById(id));
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ReadJobDefinitionDto update(@PathVariable("id") final Long id,
                                       @RequestBody @Valid final WriteJobDefinitionDto writeJobDefinitionDto) {
        try (final CloseableContext ignored = logger.with(JOB_ID, id.toString())) {
            logger.info("Updating job.");

            jobNameValidator.validate(writeJobDefinitionDto.getName(), id);

            final JobDefinition jobDefinition = jobDefinitionService.getById(id);
            jobDefinitionMapper.update(writeJobDefinitionDto, jobDefinition);
            jobDefinitionService.save(jobDefinition);

            return jobDefinitionMapper.map(jobDefinition);
        }
    }
}
