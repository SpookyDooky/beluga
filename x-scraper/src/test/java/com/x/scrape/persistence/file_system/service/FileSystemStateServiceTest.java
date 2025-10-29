package com.x.scrape.persistence.file_system.service;

import com.x.scrape.persistence.shared.event.SequenceIncrementedEvent;
import com.x.scrape.persistence.shared.model.Sequence;
import com.x.scrape.properties.persistence.PersistenceProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileSystemStateServiceTest {
	
	private static final String PERSISTENCE_FOLDER = "/folder";
	private static final Path SEQUENCE_PATH = Path.of(PERSISTENCE_FOLDER + "/state/sequences.json");
	
	@Mock
	private FileSystemService fileSystemService;
	@Mock(answer = RETURNS_DEEP_STUBS)
	private PersistenceProperties persistenceProperties;
	
	private FileSystemStateService fileSystemStateService;
	
	@BeforeEach
	void setup() {
		when(persistenceProperties.getFileSystem().getFolder()).thenReturn(PERSISTENCE_FOLDER);
		fileSystemStateService = new FileSystemStateService(fileSystemService, persistenceProperties);
	}
	
	@Test
	void shouldGetSequence() {
		final File file = new File("test");
		when(fileSystemService.get(SEQUENCE_PATH)).thenReturn(Optional.of(file));
		
		final Sequence sequence = Instancio.create(Sequence.class);
		when(fileSystemService.readFileAs(file, Sequence.class)).thenReturn(sequence);
		
		final Long sequenceCount = fileSystemStateService.getSequence();
		
		assertEquals(sequence.getSequence(), sequenceCount);
	}
	
	@Test
	void shouldCreateSequence() {
		when(fileSystemService.get(SEQUENCE_PATH)).thenReturn(Optional.empty());
		doAnswer(invocation -> invocation.getArguments()[0]).when(fileSystemService).save(any(Sequence.class), eq(SEQUENCE_PATH));
		
		final Long sequence = fileSystemStateService.getSequence();
		
		assertEquals(0, sequence);
	}
	
	@Test
	void shouldOnSequenceIncremented() {
		final SequenceIncrementedEvent event = Instancio.create(SequenceIncrementedEvent.class);
		
		final File file = new File("test");
		when(fileSystemService.get(SEQUENCE_PATH)).thenReturn(Optional.of(file));
		
		final Sequence sequence = Instancio.create(Sequence.class);
		when(fileSystemService.readFileAs(file, Sequence.class)).thenReturn(sequence);
		
		doAnswer(invocation -> invocation.getArguments()[0]).when(fileSystemService).save(sequence, SEQUENCE_PATH);
		
		fileSystemStateService.onSequenceIncremented(event);
		
		assertEquals(event.getSequence(), sequence.getSequence());
	}
	
	@Test
	void shouldThrowIllegalStateExceptionOnSequenceIncremented() {
		final SequenceIncrementedEvent event = Instancio.create(SequenceIncrementedEvent.class);
		when(fileSystemService.get(SEQUENCE_PATH)).thenReturn(Optional.empty());
		
		assertThrows(IllegalStateException.class, () -> fileSystemStateService.onSequenceIncremented(event));
	}
}