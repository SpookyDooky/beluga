package com.beluga.api.job.validation.validator;

import com.beluga.api.job.validation.annotation.JobName;
import com.beluga.service.job.JobDefinitionService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class JobNameValidator implements ConstraintValidator<JobName, String> {

    private final JobDefinitionService jobDefinitionService;

    public JobNameValidator(final JobDefinitionService jobDefinitionService) {
        this.jobDefinitionService = jobDefinitionService;
    }

    @Override
    public boolean isValid(final String jobName, final ConstraintValidatorContext context) {
        return !jobDefinitionService.existsByName(jobName);
    }
}
