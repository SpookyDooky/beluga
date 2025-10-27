package com.x.scrape.persistence.s3.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.scrape.persistence.config.conditionals.annotation.IsS3;
import com.x.scrape.persistence.shared.event.SequenceIncrementedEvent;
import com.x.scrape.persistence.shared.model.Sequence;
import com.x.scrape.persistence.shared.service.EntityIdSetterService;
import com.x.scrape.persistence.shared.service.StateService;
import com.x.scrape.properties.persistence.S3PersistenceProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

import java.nio.file.Path;
import java.util.Optional;

@IsS3
@Service
public class S3StateService extends S3PersistenceService implements StateService {
	
	public static final String STATE_FOLDER = "/state";
	public static final String SEQUENCE_FILE = "sequences.json";
	
	private final Path sequencePath;
	
	public S3StateService(@Qualifier("persistence-s3client") final S3Client s3Client,
	                      @Lazy final EntityIdSetterService entityIdSetterService,
	                      final ObjectMapper objectMapper,
	                      final S3PersistenceProperties s3PersistenceProperties) {
		super(s3Client, entityIdSetterService, objectMapper, s3PersistenceProperties);
		
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
		final Optional<Sequence> sequenceOptional = getObjectAs(sequencePath, Sequence.class);
		
		if (sequenceOptional.isPresent()) {
			return sequenceOptional.get().getSequence();
		}
		
		final Sequence sequence = new Sequence();
		sequence.setSequence(0L);
		putObject(sequencePath, sequence);
		
		return sequence.getSequence();
	}
	
	@Override
	public void onSequenceIncremented(final SequenceIncrementedEvent event) {
		final Sequence sequence = getObjectAs(sequencePath, Sequence.class)
				.orElseThrow(() -> new IllegalStateException("Could not find sequence object."));
		
		sequence.setSequence(event.getSequence());
		
		putObject(sequencePath, sequence);
	}
}
