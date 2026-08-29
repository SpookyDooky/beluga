package com.beluga.api.job.validation.validator;

import com.beluga.service.job.JobDefinitionService;
import org.springframework.stereotype.Component;

@Component
public class JobNameValidator {

    private final JobDefinitionService jobDefinitionService;

    public JobNameValidator(final JobDefinitionService jobDefinitionService) {
        this.jobDefinitionService = jobDefinitionService;
    }

    public void validate(final String jobName) {
        if (jobDefinitionService.existsByName(jobName)) {
            throw new IllegalArgumentException("Job name must be unique.");
        }
    }

    public void validate(final String jobName,
                         final Long jobDefinitionId) {
        if (!jobName.equals(jobDefinitionService.getNameById(jobDefinitionId))) {
            validate(jobName);
        }
    }
}
