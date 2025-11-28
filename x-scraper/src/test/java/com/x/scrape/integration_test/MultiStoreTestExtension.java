package com.x.scrape.integration_test;

import com.x.scrape.integration_test.datastore.FileSystemTemplateFactory;
import com.x.scrape.integration_test.datastore.PostgreSqlTemplateFactory;
import com.x.scrape.integration_test.datastore.S3TemplateFactory;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.stream.Stream;

/**
 * Test extension class that allows integration tests to run multiple times using different datastores.
 */
public class MultiStoreTestExtension implements TestTemplateInvocationContextProvider {
	
	@Override
	public boolean supportsTestTemplate(final ExtensionContext context) {
		return true;
	}
	
	@Override
	public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(final ExtensionContext context) {
		return Stream.of(
				PostgreSqlTemplateFactory.createContext(
						new PostgreSQLContainer("postgres:17.0")
								.withDatabaseName("test")
								.withUsername("username")
								.withPassword("password")
				),
				S3TemplateFactory.createContext(
						new MinIOContainer("minio/minio:latest")
								.withUserName("username")
								.withPassword("password")
				),
				FileSystemTemplateFactory.createContext()
		);
	}
}
