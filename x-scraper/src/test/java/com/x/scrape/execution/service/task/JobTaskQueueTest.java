package com.x.scrape.execution.service.task;

import com.x.scrape.execution.model.job.Job;
import com.x.scrape.execution.model.task.Task;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

class JobTaskQueueTest {
	
	private final JobTaskQueue jobTaskQueue = new JobTaskQueue();
	
	@Test
	void shouldBeEmptyIfNotPresent() {
		assertTrue(jobTaskQueue.isQueueEmpty(123L));
	}
	
	@Test
	void shouldNotBeEmpty() {
		final Task task = Instancio.of(Task.class)
				.set(field(Task::getJob), Instancio.create(Job.class))
				.create();
		
		jobTaskQueue.offerTask(task);
		
		assertFalse(jobTaskQueue.isQueueEmpty(task.getJob().getId()));
	}
	
	@Test
	void shouldBeEmptyAfterRemovingLastTask() {
		final Task task = Instancio.of(Task.class)
				.set(field(Task::getJob), Instancio.create(Job.class))
				.create();
		
		jobTaskQueue.offerTask(task);
		jobTaskQueue.pollTask(task.getJob().getId());
		
		assertTrue(jobTaskQueue.isQueueEmpty(task.getJob().getId()));
	}
	
	@Test
	void shouldReturnTask() {
		final Task task = Instancio.of(Task.class)
				.set(field(Task::getJob), Instancio.create(Job.class))
				.create();
		
		jobTaskQueue.offerTask(task);
		
		final Task result = jobTaskQueue.pollTask(task.getJob().getId())
				.get();
		
		assertSame(task, result);
	}
	
	@Test
	void shouldReturnOptionalEmpty() {
		final Task task = Instancio.of(Task.class)
				.set(field(Task::getJob), Instancio.create(Job.class))
				.create();
		
		jobTaskQueue.offerTask(task);
		jobTaskQueue.pollTask(task.getJob().getId());
		
		final Optional<Task> taskOptional = jobTaskQueue.pollTask(task.getJob().getId());
		
		assertTrue(taskOptional.isEmpty());
	}
	
	@Test
	void shouldClearTasks() {
		final Task task = Instancio.of(Task.class)
				.set(field(Task::getJob), Instancio.create(Job.class))
				.create();
		jobTaskQueue.offerTask(task);
		
		final Collection<Task> remainingTasks = jobTaskQueue.clearTasks(task.getJob().getId());
		
		assertEquals(1, remainingTasks.size());
		assertTrue(remainingTasks.contains(task));
		assertTrue(jobTaskQueue.isQueueEmpty(task.getJob().getId()));
	}
}