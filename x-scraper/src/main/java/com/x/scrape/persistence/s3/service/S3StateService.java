package com.x.scrape.persistence.s3.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.scrape.persistence.config.conditionals.annotation.IsS3;
import com.x.scrape.persistence.shared.event.SequenceIncrementedEvent;
import com.x.scrape.persistence.shared.service.EntityIdSetterService;
import com.x.scrape.persistence.shared.service.StateService;
import com.x.scrape.properties.persistence.S3PersistenceProperties;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

@IsS3
@Service
public class S3StateService extends S3PersistenceService implements StateService {
	
	public static final String STATE_FOLDER = "/state";
	public static final String SEQUENCE_FILE = "sequences.json";
	
	public S3StateService(final S3Client s3Client,
	                      final EntityIdSetterService entityIdSetterService,
	                      final ObjectMapper objectMapper,
	                      final S3PersistenceProperties s3PersistenceProperties) {
		super(s3Client, entityIdSetterService, objectMapper, s3PersistenceProperties);
	}
	
	
	@Override
	public void onSequenceIncremented(final SequenceIncrementedEvent event) {
	
	}
}
