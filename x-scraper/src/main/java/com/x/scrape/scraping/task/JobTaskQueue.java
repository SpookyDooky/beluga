package com.x.scrape.scraping.task;

import com.x.scrape.model.job.Job;
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
	
	private final Map<UUID, Queue<Task>> jobTaskQueueMap = new ConcurrentHashMap<>();
	
	/**
	 * Checks if a {@link Job}'s queue is empty
	 *
	 * @param jobId id of the {@link Job}
	 * @return true if empty or null.
	 */
	public boolean isQueueEmpty(final UUID jobId) {
		if (jobTaskQueueMap.containsKey(jobId)) {
			return jobTaskQueueMap.get(jobId).isEmpty();
		}
		
		return true;
	}
	
	/**
	 * Adds a new task to a {@link Job}'s queue.
	 *
	 * @param task  to add.
	 */
	public void offerTask(final Task task) {
		jobTaskQueueMap.compute(
				task.getJobId(),
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
	 * Returns the next {@link Task} for a {@link Job}.
	 *
	 * @param jobId id of the {@link Job}
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
