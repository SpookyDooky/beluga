package com.x.scrape.integration_test.datastore;

import com.x.scrape.integration_test.properties.TestContainerProperties;
import org.junit.jupiter.api.extension.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class PostgreSqlTemplateFactory {
	
	private static final String PROFILE = "postgresql";
	
	public static TestTemplateInvocationContext createContext(final GenericContainer<?> container) {
		if (container instanceof PostgreSQLContainer postgreSQLContainer) {
			return createContext(postgreSQLContainer);
		}
		
		throw new IllegalArgumentException("Unsupported container: " + container.getClass().getSimpleName());
	}
	
	private static TestTemplateInvocationContext createContext(final PostgreSQLContainer container) {
		return new TestTemplateInvocationContext() {
			
			@Override
			public String getDisplayName(int invocationIndex) {
				return "PostgreSQL";
			}
			
			@Override
			public List<Extension> getAdditionalExtensions() {
				return createExtensions(container);
			}
		};
	}
	
	private static List<Extension> createExtensions(final PostgreSQLContainer container) {
		final List<Extension> extensions = new ArrayList<>();
		
		extensions.add(createInvocationInterceptor(container));
		extensions.add(createAfterAll(container));
		
		return extensions;
	}
	
	private static Extension createInvocationInterceptor(final PostgreSQLContainer container) {
		return new InvocationInterceptor() {
			@Override
			public <T> T interceptTestClassConstructor(final Invocation<T> invocation, final ReflectiveInvocationContext<Constructor<T>> invocationContext, final ExtensionContext extensionContext) throws Throwable {
				TestContainerProperties.setCurrentProfile(PROFILE, container);
				container.start();
				
				System.setProperty("POSTGRE_HOST", container.getJdbcUrl() + "&currentSchema=x_scraper");
				
				return InvocationInterceptor.super.interceptTestClassConstructor(invocation, invocationContext, extensionContext);
			}
		};
	}
	
	private static Extension createAfterAll(final PostgreSQLContainer container) {
		return (AfterAllCallback) context -> {
			container.stop();
		};
	}
}
