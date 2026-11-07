package com.beluga.execution.service.job;

import com.beluga.execution.model.job.Job;
import com.beluga.model.job_definition.JobExecution;
import com.beluga.service.job.JobExecutionService;
import com.beluga.service.job.JobService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.beluga.model.job_definition.JobStatus.ACTIVE;
import static com.beluga.model.job_definition.JobStatus.PLANNED;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobSchedulerTest {

	@Mock
	private JobExecutionService jobExecutionService;
	@Mock
	private JobService jobService;
	@Mock
	private JobExecutorService jobExecutorService;

	@InjectMocks
	private JobScheduler jobScheduler;

	@Test
	void shouldStartOutstandingJobs() {
		final JobExecution firstJobExecution = mock();
		final JobExecution secondJobExecution = mock();
		when(jobExecutionService.findAllByStatus(List.of(PLANNED, ACTIVE))).thenReturn(List.of(firstJobExecution, secondJobExecution));

		final Job firstJob = new Job();
		final Job secondJob = new Job();
		when(jobService.createActiveJob(firstJobExecution)).thenReturn(firstJob);
		when(jobService.createActiveJob(secondJobExecution)).thenReturn(secondJob);

		jobScheduler.startOutstandingJobs();

		verify(jobExecutorService).execute(firstJob);
		verify(jobExecutorService).execute(secondJob);
	}

	@Test
	void shouldNotStartAnythingWithoutOutstandingJobs() {
		when(jobExecutionService.findAllByStatus(List.of(PLANNED, ACTIVE))).thenReturn(List.of());

		jobScheduler.startOutstandingJobs();

		verifyNoInteractions(jobService, jobExecutorService);
	}
}
