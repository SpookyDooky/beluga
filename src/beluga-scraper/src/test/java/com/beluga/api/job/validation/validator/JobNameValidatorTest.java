package com.beluga.api.job.validation.validator;

import com.beluga.service.job.JobDefinitionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobNameValidatorTest {

    @Mock
    private JobDefinitionService jobDefinitionService;

    @InjectMocks
    private JobNameValidator jobNameValidator;

    @Test
    void shouldValidate() {
        final String jobName = "jobName";
        when(jobDefinitionService.existsByName(jobName)).thenReturn(false);

        jobNameValidator.validate(jobName);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForValidate() {
        final String jobName = "jobName";
        when(jobDefinitionService.existsByName(jobName)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> jobNameValidator.validate(jobName));
    }

    @Test
    void shouldValidateForNameAndId() {
        final String jobName = "jobName";
        final Long jobDefinitionId = 123L;
        when(jobDefinitionService.getNameById(jobDefinitionId)).thenReturn(jobName);

        jobNameValidator.validate(jobName, jobDefinitionId);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForValidationForNameAndId() {
        final String jobName = "jobName";
        final Long jobDefinitionId = 123L;
        when(jobDefinitionService.getNameById(jobDefinitionId)).thenReturn("otherName");
        when(jobDefinitionService.existsByName(jobName)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> jobNameValidator.validate(jobName, jobDefinitionId));
    }
}