package com.x.scrape.scraping.task;

import com.x.scrape.model.job.JobDefinition;
import com.x.scrape.model.task.Task;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

class JobDefinitionTaskQueueTest {
	
	private final JobTaskQueue jobTaskQueue = new JobTaskQueue();
	
	@Test
	void shouldBeEmptyIfNotPresent() {
		assertTrue(jobTaskQueue.isQueueEmpty(UUID.randomUUID()));
	}
	
	@Test
	void shouldNotBeEmpty() {
		final Task task = Instancio.of(Task.class)
				.set(field(Task::getJob), Instancio.create(JobDefinition.class))
				.create();
		
		jobTaskQueue.offerTask(task);
		
		assertFalse(jobTaskQueue.isQueueEmpty(task.getJob().getUuid()));
	}
	
	@Test
	void shouldBeEmptyAfterRemovingLastTask() {
		final Task task = Instancio.of(Task.class)
				.set(field(Task::getJob), Instancio.create(JobDefinition.class))
				.create();
		
		jobTaskQueue.offerTask(task);
		jobTaskQueue.pollTask(task.getJob().getUuid());
		
		assertTrue(jobTaskQueue.isQueueEmpty(task.getJob().getUuid()));
	}
	
	@Test
	void shouldReturnTask() {
		final Task task = Instancio.of(Task.class)
				.set(field(Task::getJob), Instancio.create(JobDefinition.class))
				.create();
		
		jobTaskQueue.offerTask(task);
		
		final Task result = jobTaskQueue.pollTask(task.getJob().getUuid())
				.get();
		
		assertSame(task, result);
	}
	
	@Test
	void shouldReturnOptionalEmpty() {
		final Task task = Instancio.of(Task.class)
				.set(field(Task::getJob), Instancio.create(JobDefinition.class))
				.create();
		
		jobTaskQueue.offerTask(task);
		jobTaskQueue.pollTask(task.getJob().getUuid());
		
		final Optional<Task> taskOptional = jobTaskQueue.pollTask(task.getJob().getUuid());
		
		assertTrue(taskOptional.isEmpty());
	}
}