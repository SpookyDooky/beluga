package com.x.scrape.persistence.file_system.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.scrape.persistence.config.conditionals.IsFileSystem;
import com.x.scrape.persistence.shared.model.Sequence;
import com.x.scrape.persistence.shared.event.SequenceIncrementedEvent;
import com.x.scrape.persistence.shared.service.EntityIdSetterService;
import com.x.scrape.persistence.shared.service.StateService;
import com.x.scrape.properties.persistence.PersistenceProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

@Service
@IsFileSystem
public class FileSystemStateService extends FileSystemService
		implements StateService {
	
	public static final String STATE_FOLDER = "/state";
	public static final String SEQUENCE_FILE = "sequences.json";
	
	private final Path sequencePath;
	
	public FileSystemStateService(final ObjectMapper objectMapper,
	                              final PersistenceProperties persistenceProperties,
	                              @Lazy final EntityIdSetterService entityIdSetterService) {
		super(objectMapper, entityIdSetterService);
		
		final String persistenceFolder = persistenceProperties.getFileSystem().getFolder();
		sequencePath = Path.of(persistenceFolder + STATE_FOLDER + "/" + SEQUENCE_FILE);
	}
	
	/**
	 * Retrieves the current sequence from the file system.
	 * If the sequence does not exist it will create the sequence and return it.
	 *
	 * @return the current sequence count.
	 */
	public Long getSequence() {
		final Optional<File> sequenceFileOptional = get(sequencePath);
		
		final Sequence sequence;
		if (sequenceFileOptional.isPresent()) {
			sequence = readFileAs(sequenceFileOptional.get(), Sequence.class);
		} else {
			sequence = new Sequence();
			sequence.setSequence(0L);
			save(sequence, sequencePath);
		}
		
		return sequence.getSequence();
	}
	
	@Override
	@EventListener
	public void onSequenceIncremented(final SequenceIncrementedEvent event) {
		final File sequenceFile = get(sequencePath)
				.orElseThrow(() -> new IllegalStateException("Could not find sequence file."));
		
		final Sequence sequence = readFileAs(sequenceFile, Sequence.class);
		sequence.setSequence(event.getSequence());
		
		save(sequence, sequencePath);
	}
}
