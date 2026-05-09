package com.beluga.service.task;

import com.beluga.model.job_definition.JobExecution;
import com.beluga.model.result.ResultFile;
import com.beluga.execution.model.task.TaskExecution;
import com.beluga.execution.model.task.TaskStatus;
import com.beluga.persistence.repository.TaskExecutionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
	
	@Transactional(propagation = MANDATORY)
	public List<TaskExecution> saveAll(final List<TaskExecution> taskExecutions) {
		return taskExecutionRepository.saveAll(taskExecutions);
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
	
	@Transactional(propagation = MANDATORY)
	public Page<TaskExecution> findByJobExecutionPaged(final JobExecution jobExecution,
	                                                   final Pageable pageable) {
		return taskExecutionRepository.findByJobExecution(jobExecution, pageable);
	}
}
