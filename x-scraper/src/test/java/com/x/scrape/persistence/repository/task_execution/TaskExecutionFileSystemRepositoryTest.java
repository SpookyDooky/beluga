package com.x.scrape.persistence.repository.task_execution;

import com.x.scrape.persistence.file_system.service.FileSystemService;
import com.x.scrape.persistence.shared.service.EntityIdSetterService;
import com.x.scrape.properties.persistence.PersistenceProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskExecutionFileSystemRepositoryTest {
	
	@Mock
	private FileSystemService fileSystemService;
	@Mock
	private PersistenceProperties persistenceProperties;
	@Mock
	private EntityIdSetterService entityIdSetterService;
	
	@InjectMocks
	private TaskExecutionFileSystemRepository taskExecutionFileSystemRepository;
	
	@Test
	void shouldSave() {
		Assertions.fail();
	}
	
	@Test
	void shouldFindById() {
		Assertions.fail();
	}
	
	@Test
	void shouldFindByIdReturnOptionalEmpty() {
		Assertions.fail();
	}
	
	@Test
	void shouldCreateTaskExecutionIndex() {
		Assertions.fail();
	}
}