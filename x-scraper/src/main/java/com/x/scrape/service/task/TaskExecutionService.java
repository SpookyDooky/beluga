package com.x.scrape.service.task;

import com.x.scrape.model.result.ResultFile;
import com.x.scrape.model.task.TaskExecution;
import com.x.scrape.model.task.TaskStatus;
import com.x.scrape.persistence.repository.task_execution.TaskExecutionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Service
public class TaskExecutionService {
	
	private final TaskExecutionRepository taskExecutionRepository;
	
	public TaskExecutionService(final TaskExecutionRepository taskExecutionRepository) {
		this.taskExecutionRepository = taskExecutionRepository;
	}
	
	@Transactional
	public Optional<TaskExecution> findById(final Long id) {
		return taskExecutionRepository.findById(id);
	}
	
	@Transactional(propagation = MANDATORY)
	public TaskExecution getById(final Long id) {
		return taskExecutionRepository.findById(id)
				.orElseThrow(EntityNotFoundException::new);
	}
	
	@Transactional(propagation = MANDATORY)
	public TaskExecution save(final TaskExecution taskExecution) {
		return taskExecutionRepository.save(taskExecution);
	}
	
	@Transactional
	public void setStatusById(final Long id,
	                          final TaskStatus status) {
		final TaskExecution taskExecution = getById(id);
		taskExecution.setStatus(status);
		
		taskExecutionRepository.save(taskExecution);
	}
	
	/**
	 * Adds a {@link ResultFile} to a {@link TaskExecution}.
	 *
	 * @param id         of the {@link TaskExecution}.
	 * @param resultFile the {@link ResultFile} to add.
	 */
	@Transactional
	public void addResultFile(final Long id,
	                          final ResultFile resultFile) {
		final TaskExecution taskExecution = getById(id);
		taskExecution.addResultFile(resultFile);
		
		taskExecutionRepository.save(taskExecution);
		
	}
}
