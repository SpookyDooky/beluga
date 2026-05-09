package com.beluga.integration_test;

import com.beluga.integration_test.datastore.PostgreSqlTemplateFactory;
import com.beluga.integration_test.datastore.SqliteTemplateFactory;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

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
				PostgreSqlTemplateFactory.createContext(),
				SqliteTemplateFactory.createContext()
		);
	}
}
