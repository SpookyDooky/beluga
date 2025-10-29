package com.x.scrape.persistence.s3.service;

import com.x.scrape.persistence.config.conditionals.annotation.IsS3;
import com.x.scrape.persistence.shared.event.SequenceIncrementedEvent;
import com.x.scrape.persistence.shared.model.Sequence;
import com.x.scrape.persistence.shared.service.StateService;
import com.x.scrape.properties.persistence.S3PersistenceProperties;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Optional;

@IsS3
@Service
public class S3StateService implements StateService {
	
	public static final String STATE_FOLDER = "/state";
	public static final String SEQUENCE_FILE = "sequences.json";
	
	private final S3PersistenceService s3PersistenceService;
	private final Path sequencePath;
	
	public S3StateService(final S3PersistenceService s3PersistenceService,
	                      final S3PersistenceProperties s3PersistenceProperties) {
		this.s3PersistenceService = s3PersistenceService;
		
		final String persistenceFolder = s3PersistenceProperties.getFolder();
		sequencePath = Path.of(persistenceFolder + STATE_FOLDER + "/" + SEQUENCE_FILE);
	}
	
	/**
	 * Retrieves the current sequence from a S3 compatible object store.
	 * If the sequence does not exist it will create the sequence and return it.
	 *
	 * @return the current sequence count.
	 */
	public Long getSequence() {
		final Optional<Sequence> sequenceOptional = s3PersistenceService.getObjectAs(sequencePath, Sequence.class);
		
		if (sequenceOptional.isPresent()) {
			return sequenceOptional.get().getSequence();
		}
		
		final Sequence sequence = new Sequence();
		sequence.setSequence(0L);
		s3PersistenceService.putObject(sequencePath, sequence);
		
		return sequence.getSequence();
	}
	
	@Override
	@EventListener
	public void onSequenceIncremented(final SequenceIncrementedEvent event) {
		final Sequence sequence = s3PersistenceService.getObjectAs(sequencePath, Sequence.class)
				.orElseThrow(() -> new IllegalStateException("Could not find sequence object."));
		
		sequence.setSequence(event.getSequence());
		
		s3PersistenceService.putObject(sequencePath, sequence);
	}
}
