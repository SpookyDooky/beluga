package com.x.scrape.persistence.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;

@Configuration
@ConditionalOnMissingBean(PlatformTransactionManager.class)
public class NoOperationTransactionalConfig {
	
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
