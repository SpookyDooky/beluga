package com.x.scrape.persistence.file_system.config;

import com.x.scrape.persistence.config.conditionals.IsFileSystem;
import com.x.scrape.persistence.file_system.service.FileSystemStateService;
import com.x.scrape.persistence.shared.service.PersistenceIdService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@IsFileSystem
@Configuration
public class FileSystemPersistenceConfig {

	@Bean
	public PersistenceIdService persistenceIdService(final FileSystemStateService fileSystemStateService,
	                                                 final ApplicationEventPublisher applicationEventPublisher) {
		final Long sequence = fileSystemStateService.getSequence();
		return new PersistenceIdService(sequence, applicationEventPublisher);
	}
}
