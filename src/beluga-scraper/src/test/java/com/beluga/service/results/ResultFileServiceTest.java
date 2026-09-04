package com.beluga.service.results;

import com.beluga.execution.model.task.TaskExecution;
import com.beluga.model.job_definition.JobExecution;
import com.beluga.model.result.ResultFile;
import com.beluga.persistence.repository.ResultFileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResultFileServiceTest {

    @Mock
    private ResultFileRepository resultFileRepository;

    @InjectMocks
    private ResultFileService resultFileService;

    @Test
    void shouldFindByTaskExecutionAndKey() {
        final TaskExecution taskExecution = mock();
        final String key = "key";

        final Optional<ResultFile> resultFile = mock();
        when(resultFileRepository.findByTaskExecutionAndKey(taskExecution, key)).thenReturn(resultFile);

        final Optional<ResultFile> result = resultFileService.findByTaskExecutionAndKey(taskExecution, key);

        assertSame(resultFile, result);
    }

    @Test
    void shouldFindByJobExecutionAndKeyPaged() {
        final JobExecution jobExecution = mock();
        final String key = "key";
        final Pageable pageable = mock();

        final Page<ResultFile> page = mock();
        when(resultFileRepository.findByTaskExecution_JobExecutionAndKey(jobExecution, key, pageable)).thenReturn(page);

        final Page<ResultFile> result = resultFileService.findByJobExecutionAndKeyPaged(jobExecution, key, pageable);

        assertSame(page, result);
    }
}