package com.x.scrape.scraping.task;

import com.x.scrape.scraping.task.JobTaskQueue;
import com.x.scrape.model.task.Task;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

class JobTaskQueueTest {
	
	private final JobTaskQueue jobTaskQueue = new JobTaskQueue();
	
	@Test
	void shouldBeEmptyIfNotPresent() {
		assertTrue(jobTaskQueue.isQueueEmpty(UUID.randomUUID()));
	}
	
	@Test
	void shouldNotBeEmpty() {
		final UUID jobId = UUID.randomUUID();
		final Task task = Instancio.of(Task.class)
				.set(field(Task::getJobId), jobId)
				.create();
		
		jobTaskQueue.offerTask(task);
		
		assertFalse(jobTaskQueue.isQueueEmpty(jobId));
	}
	
	@Test
	void shouldBeEmptyAfterRemovingLastTask() {
		final UUID jobId = UUID.randomUUID();
		final Task task = Instancio.of(Task.class)
				.set(field(Task::getJobId), jobId)
				.create();
		
		jobTaskQueue.offerTask(task);
		jobTaskQueue.pollTask(jobId);
		
		assertTrue(jobTaskQueue.isQueueEmpty(jobId));
	}
	
	@Test
	void shouldReturnTask() {
		final UUID jobId = UUID.randomUUID();
		final Task task = Instancio.of(Task.class)
				.set(field(Task::getJobId), jobId)
				.create();
		
		jobTaskQueue.offerTask(task);
		
		final Task result = jobTaskQueue.pollTask(jobId)
				.get();
		
		assertSame(task, result);
	}
	
	@Test
	void shouldReturnOptionalEmpty() {
		final UUID jobId = UUID.randomUUID();
		final Task task = Instancio.of(Task.class)
				.set(field(Task::getJobId), jobId)
				.create();
		
		jobTaskQueue.offerTask(task);
		jobTaskQueue.pollTask(jobId);
		
		final Optional<Task> taskOptional = jobTaskQueue.pollTask(jobId);
		
		assertTrue(taskOptional.isEmpty());
	}
}