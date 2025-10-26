package com.x.scrape.execution.service.task;

import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.task.Task;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class JobTaskQueue {
	
	// TODO - Should be based on a Long id
	private final Map<UUID, Queue<Task>> jobTaskQueueMap = new ConcurrentHashMap<>();
	
	/**
	 * Checks if a {@link JobDefinition}'s queue is empty
	 *
	 * @param jobId id of the {@link JobDefinition}
	 * @return true if empty or null.
	 */
	public boolean isQueueEmpty(final UUID jobId) {
		if (jobTaskQueueMap.containsKey(jobId)) {
			return jobTaskQueueMap.get(jobId).isEmpty();
		}
		
		return true;
	}
	
	/**
	 * Adds a new task to a {@link JobDefinition}'s queue.
	 *
	 * @param task  to add.
	 */
	public void offerTask(final Task task) {
		jobTaskQueueMap.compute(
				task.getJob().getUuid(),
				(key, jobTaskQueue) -> {
					if (jobTaskQueue == null) {
						jobTaskQueue = new ConcurrentLinkedQueue<>();
					}
					jobTaskQueue.offer(task);
					
					return jobTaskQueue;
				}
		);
	}
	
	/**
	 * Returns the next {@link Task} for a {@link JobDefinition}.
	 *
	 * @param jobId id of the {@link JobDefinition}
	 * @return optional task, empty if no task is present.
	 */
	public Optional<Task> pollTask(final UUID jobId) {
		if (isQueueEmpty(jobId)) {
			return Optional.empty();
		}
		
		return Optional.ofNullable(
				jobTaskQueueMap
						.get(jobId)
						.poll()
		);
	}
}
