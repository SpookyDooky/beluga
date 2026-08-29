package com.beluga.api.job.validation.validator;

import com.beluga.service.job.JobDefinitionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobNameValidatorTest {

    @Mock
    private JobDefinitionService jobDefinitionService;

    @InjectMocks
    private JobNameValidator jobNameValidator;

    @Test
    void shouldBeValid() {
        final String jobName = "jobName";
        when(jobDefinitionService.existsByName(jobName)).thenReturn(false);

        assertTrue(jobNameValidator.isValid(jobName, null));
    }

    @Test
    void shouldNotBeValid() {
        final String jobName = "jobName";
        when(jobDefinitionService.existsByName(jobName)).thenReturn(true);

        assertFalse(jobNameValidator.isValid(jobName, null));
    }
}