package com.x.scrape.persistence.repository.task_execution;

import com.x.scrape.model.job_definition.JobDefinition;
import com.x.scrape.model.job_definition.JobExecution;
import com.x.scrape.model.task.TaskExecution;
import com.x.scrape.persistence.file_system.service.FileSystemService;
import com.x.scrape.persistence.shared.model.TaskExecutionIndex;
import com.x.scrape.persistence.shared.service.EntityIdSetterService;
import com.x.scrape.properties.persistence.PersistenceProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskExecutionFileSystemRepositoryTest {
	
	private static final String FILE_SYSTEM_PERSISTENCE_FOLDER = "/folder";
	private static final String JOB_DEFINITION_PERSISTENCE_SUB_PATH = "/job-definitions";
	private static final String TASK_EXECUTIONS_SUB_PATH = "/task-executions";
	
	@Mock
	private FileSystemService fileSystemService;
	@Mock(answer = RETURNS_DEEP_STUBS)
	private PersistenceProperties persistenceProperties;
	@Mock
	private EntityIdSetterService entityIdSetterService;
	
	@InjectMocks
	private TaskExecutionFileSystemRepository taskExecutionFileSystemRepository;
	
	@Captor
	private ArgumentCaptor<TaskExecutionIndex> taskExecutionIndexArgumentCaptor;
	
	@BeforeEach
	void setup() {
		when(persistenceProperties.getFileSystem().getFolder()).thenReturn(FILE_SYSTEM_PERSISTENCE_FOLDER);
	}
	
	@Test
	void shouldSave() {
		taskExecutionFileSystemRepository = spy(taskExecutionFileSystemRepository);
		
		final TaskExecution taskExecution = Instancio.create(TaskExecution.class);
		doNothing().when(taskExecutionFileSystemRepository).updateIndex(taskExecution);
		
		final Path taskExecutionPath = Path.of(
				persistenceProperties.getFileSystem().getFolder() +
						JOB_DEFINITION_PERSISTENCE_SUB_PATH + "/" +
						taskExecution.getJobExecution().getJobDefinition().getId() +
						TASK_EXECUTIONS_SUB_PATH + "/" + taskExecution.getId() + ".json"
		);
		when(fileSystemService.save(taskExecution, taskExecutionPath)).thenReturn(taskExecution);
		
		final TaskExecution result = taskExecutionFileSystemRepository.save(taskExecution);
		
		assertSame(taskExecution, result);
		verify(entityIdSetterService).setIds(taskExecution);
	}
	
	@Test
	void shouldFindById() {
		final Path jobDefinitionsPath = Path.of(FILE_SYSTEM_PERSISTENCE_FOLDER + JOB_DEFINITION_PERSISTENCE_SUB_PATH);
		
		final File jobDefinitionFile = mock();
		when(jobDefinitionFile.getPath()).thenReturn(jobDefinitionsPath.toString());
		when(fileSystemService.listFiles(jobDefinitionsPath)).thenReturn(List.of(jobDefinitionFile));
		
		final TaskExecution taskExecution = Instancio.create(TaskExecution.class);
		final TaskExecutionIndex taskExecutionIndex = Instancio.of(TaskExecutionIndex.class)
				.set(field(TaskExecutionIndex::getIds), Set.of(taskExecution.getId()))
				.create();
		
		final Path taskExecutionIndexPath = Path.of(jobDefinitionsPath + "/task-execution-index.json");
		when(fileSystemService.readFileAs(taskExecutionIndexPath.toFile(), TaskExecutionIndex.class)).thenReturn(taskExecutionIndex);
		
		final Path taskExecutionPath = Path.of(jobDefinitionFile.getAbsolutePath() + TASK_EXECUTIONS_SUB_PATH + "/" + taskExecution.getId() + ".json");
		when(fileSystemService.readFileAs(taskExecutionPath.toFile(), TaskExecution.class)).thenReturn(taskExecution);
		
		final JobDefinition jobDefinition = mock();
		final JobExecution jobExecution = mock();
		when(jobDefinition.getExecutionById(taskExecution.getJobExecution().getId())).thenReturn(jobExecution);
		
		final Path jobDefinitionPath = Path.of(taskExecutionPath.getParent().getParent().toString() + "/job-definition.json");
		when(fileSystemService.readFileAs(jobDefinitionPath.toFile(), JobDefinition.class)).thenReturn(jobDefinition);
		
		final TaskExecution result = taskExecutionFileSystemRepository.findById(taskExecution.getId())
				.get();
		
		assertSame(taskExecution, result);
		assertSame(jobExecution, taskExecution.getJobExecution());
	}
	
	@Test
	void shouldFindByIdReturnOptionalEmpty() {
		final Path jobDefinitionsPath = Path.of(FILE_SYSTEM_PERSISTENCE_FOLDER + JOB_DEFINITION_PERSISTENCE_SUB_PATH);
		
		final File jobDefinitionFile = mock();
		when(jobDefinitionFile.getPath()).thenReturn(jobDefinitionsPath.toString());
		when(fileSystemService.listFiles(jobDefinitionsPath)).thenReturn(List.of(jobDefinitionFile));
		
		final TaskExecution taskExecution = Instancio.create(TaskExecution.class);
		final TaskExecutionIndex taskExecutionIndex = Instancio.of(TaskExecutionIndex.class)
				.set(field(TaskExecutionIndex::getIds), Set.of())
				.create();
		
		final Path taskExecutionIndexPath = Path.of(jobDefinitionsPath + "/task-execution-index.json");
		when(fileSystemService.readFileAs(taskExecutionIndexPath.toFile(), TaskExecutionIndex.class)).thenReturn(taskExecutionIndex);
		
		assertTrue(taskExecutionFileSystemRepository.findById(taskExecution.getId()).isEmpty());
	}
	
	@Test
	void shouldCreateIndex() {
		final TaskExecution taskExecution = Instancio.create(TaskExecution.class);
		
		final Path taskExecutionIndexPath = Path.of(
				FILE_SYSTEM_PERSISTENCE_FOLDER + JOB_DEFINITION_PERSISTENCE_SUB_PATH
						+ "/" + taskExecution.getJobExecution().getJobDefinition().getId() + "/task-execution-index.json"
		);
		when(fileSystemService.get(taskExecutionIndexPath)).thenReturn(Optional.empty());
		
		taskExecutionFileSystemRepository.updateIndex(taskExecution);
		
		verify(fileSystemService).save(taskExecutionIndexArgumentCaptor.capture(), eq(taskExecutionIndexPath));
		
		final TaskExecutionIndex taskExecutionIndex = taskExecutionIndexArgumentCaptor.getValue();
		assertEquals(1, taskExecutionIndex.getIds().size());
		assertTrue(taskExecutionIndex.getIds().contains(taskExecution.getId()));
	}
	
	@Test
	void shouldUpdateIndex() {
		final TaskExecution taskExecution = Instancio.create(TaskExecution.class);
		
		final Path taskExecutionIndexPath = Path.of(
				FILE_SYSTEM_PERSISTENCE_FOLDER + JOB_DEFINITION_PERSISTENCE_SUB_PATH
						+ "/" + taskExecution.getJobExecution().getJobDefinition().getId() + "/task-execution-index.json"
		);
		final File taskExecutionIndexFile = mock();
		when(fileSystemService.get(taskExecutionIndexPath)).thenReturn(Optional.of(taskExecutionIndexFile));
		
		final TaskExecutionIndex taskExecutionIndex = mock(RETURNS_DEEP_STUBS);
		when(fileSystemService.readFileAs(taskExecutionIndexFile, TaskExecutionIndex.class)).thenReturn(taskExecutionIndex);
		
		taskExecutionFileSystemRepository.updateIndex(taskExecution);
		
		verify(fileSystemService).save(taskExecutionIndex, taskExecutionIndexPath);
		verify(taskExecutionIndex.getIds()).add(taskExecution.getId());
	}
}