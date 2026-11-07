package com.beluga.service.job;

import com.beluga.model.job_definition.JobExecution;
import com.beluga.model.job_definition.JobStatus;
import com.beluga.persistence.repository.JobExecutionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobExecutionServiceTest {

	@Mock
	private JobExecutionRepository jobExecutionRepository;

	@InjectMocks
	private JobExecutionService jobExecutionService;

	@Test
	void shouldFindAllByStatus() {
		final Set<JobStatus> statuses = Set.of(JobStatus.ACTIVE, JobStatus.PAUSED);
		final JobExecution jobExecution = mock();
		final List<JobExecution> jobExecutions = List.of(jobExecution);
		when(jobExecutionRepository.findByStatusIn(statuses)).thenReturn(jobExecutions);

		assertSame(jobExecutions, jobExecutionService.findAllByStatus(statuses));
	}
}
