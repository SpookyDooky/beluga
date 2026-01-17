package com.x.scrape.persistence.file_system.service;

import com.x.scrape.persistence.config.conditionals.annotation.IsFileSystem;
import com.x.scrape.persistence.shared.event.SequenceIncrementedEvent;
import com.x.scrape.persistence.shared.model.Sequence;
import com.x.scrape.persistence.shared.service.StateService;
import com.x.scrape.properties.persistence.PersistenceProperties;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

@Service
@IsFileSystem
@Deprecated(forRemoval = true) // Due to SQL lite being a much better alternative
public class FileSystemStateService implements StateService {
	
	public static final String STATE_FOLDER = "/state";
	public static final String SEQUENCE_FILE = "sequences.json";
	
	private final FileSystemService fileSystemService;
	private final Path sequencePath;
	
	public FileSystemStateService(final FileSystemService fileSystemService,
	                              final PersistenceProperties persistenceProperties) {
		this.fileSystemService = fileSystemService;
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
		final Optional<File> sequenceFileOptional = fileSystemService.get(sequencePath);
		
		final Sequence sequence;
		if (sequenceFileOptional.isPresent()) {
			sequence = fileSystemService.readFileAs(sequenceFileOptional.get(), Sequence.class);
		} else {
			sequence = new Sequence();
			sequence.setSequence(0L);
			fileSystemService.save(sequence, sequencePath);
		}
		
		return sequence.getSequence();
	}
	
	@Override
	@EventListener
	public void onSequenceIncremented(final SequenceIncrementedEvent event) {
		final File sequenceFile = fileSystemService.get(sequencePath)
				.orElseThrow(() -> new IllegalStateException("Could not find sequence file."));
		
		final Sequence sequence = fileSystemService.readFileAs(sequenceFile, Sequence.class);
		sequence.setSequence(event.getSequence());
		
		fileSystemService.save(sequence, sequencePath);
	}
}
