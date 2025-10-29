package com.x.scrape.persistence.file_system.config;

import com.x.scrape.persistence.config.conditionals.annotation.IsFileSystem;
import com.x.scrape.persistence.file_system.service.FileSystemStateService;
import com.x.scrape.persistence.shared.service.PersistenceIdService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;

@IsFileSystem
@Configuration
public class FileSystemPersistenceConfig {

	@Bean
	public PersistenceIdService persistenceIdService(final FileSystemStateService fileSystemStateService,
	                                                 final ApplicationEventPublisher applicationEventPublisher) {
		final Long sequence = fileSystemStateService.getSequence();
		return new PersistenceIdService(sequence, applicationEventPublisher);
	}
	
	@Bean
	public PlatformTransactionManager noOperationTransactionManager() {
		return new PlatformTransactionManager() {
			@Override
			public TransactionStatus getTransaction(final TransactionDefinition definition) throws TransactionException {
				return new SimpleTransactionStatus();
			}
			
			@Override
			public void commit(final TransactionStatus status) throws TransactionException {
			
			}
			
			@Override
			public void rollback(final TransactionStatus status) throws TransactionException {
			
			}
		};
	}
	
}
