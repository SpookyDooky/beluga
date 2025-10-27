package com.x.scrape.persistence.s3.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.scrape.persistence.shared.event.SequenceIncrementedEvent;
import com.x.scrape.persistence.shared.model.Sequence;
import com.x.scrape.persistence.shared.service.EntityIdSetterService;
import com.x.scrape.properties.persistence.S3PersistenceProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.S3Client;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3StateServiceTest {
	
	private static final String PERSISTENCE_FOLDER = "/folder";
	private static final Path SEQUENCE_PATH = Path.of(PERSISTENCE_FOLDER + "/state/sequences.json");
	
	@Mock
	private S3Client s3Client;
	@Mock
	private EntityIdSetterService entityIdSetterService;
	@Mock
	private ObjectMapper objectMapper;
	@Mock
	private S3PersistenceProperties s3PersistenceProperties;
	
	private S3StateService s3StateService;
	
	@Captor
	private ArgumentCaptor<Sequence> sequenceArgumentCaptor;
	
	@BeforeEach
	void setup() {
		when(s3PersistenceProperties.getFolder()).thenReturn(PERSISTENCE_FOLDER);
		s3StateService = spy(new S3StateService(
				s3Client,
				entityIdSetterService,
				objectMapper,
				s3PersistenceProperties
		));
	}
	
	@Test
	void shouldGetSequence() {
		final Sequence sequence = Instancio.create(Sequence.class);
		doReturn(Optional.of(sequence)).when(s3StateService).getObjectAs(SEQUENCE_PATH, Sequence.class);
		
		final Long result = s3StateService.getSequence();
		
		assertEquals(sequence.getSequence(), result);
	}
	
	@Test
	void shouldCreateSequence() {
		doReturn(Optional.empty()).when(s3StateService).getObjectAs(SEQUENCE_PATH, Sequence.class);
		doReturn(mock()).when(s3StateService).putObject(eq(SEQUENCE_PATH), any(Sequence.class));
		
		final Long result = s3StateService.getSequence();
		
		assertEquals(0, result);
		
		verify(s3StateService).putObject(eq(SEQUENCE_PATH), sequenceArgumentCaptor.capture());
		final Sequence sequence = sequenceArgumentCaptor.getValue();
		assertEquals(0, sequence.getSequence());
	}
	
	@Test
	void shouldOnSequenceIncremented() {
		final Sequence sequence = mock();
		doReturn(Optional.of(sequence)).when(s3StateService).getObjectAs(SEQUENCE_PATH, Sequence.class);
		doReturn(mock()).when(s3StateService).putObject(eq(SEQUENCE_PATH), any(Sequence.class));
		
		final SequenceIncrementedEvent event = Instancio.create(SequenceIncrementedEvent.class);
		
		s3StateService.onSequenceIncremented(event);
		
		verify(sequence).setSequence(event.getSequence());
		verify(s3StateService).putObject(SEQUENCE_PATH, sequence);
	}
	
	@Test
	void shouldThrowIllegalStateExceptionOnSequenceIncrementedEvent() {
		doReturn(Optional.empty()).when(s3StateService).getObjectAs(SEQUENCE_PATH, Sequence.class);
		
		assertThrows(IllegalStateException.class, () -> {
			s3StateService.onSequenceIncremented(mock());
		});
	}
}