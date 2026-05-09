package com.beluga.integration_test.datastore;

import com.beluga.integration_test.ContainerRegistry;
import com.beluga.integration_test.properties.TestContainerProperties;
import org.junit.jupiter.api.extension.*;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import static com.beluga.properties.persistence.PersistenceType.POSTGRESQL;

public class PostgreSqlTemplateFactory {
	
	private static final String POSTGRES_IMAGE = "postgres:17.0";
	private static final String DATABASE_NAME = "test";
	
	private static final String DB_USERNAME = "username";
	private static final String DB_PASSWORD = "password";
	
	private static final String PROFILE = "postgresql";
	private static PostgreSQLContainer container;
	
	public static TestTemplateInvocationContext createContext() {
		startContainer();
		return createTestTemplateContext();
	}
	
	private static void startContainer() {
		if (!ContainerRegistry.isContainerRunning(POSTGRESQL)) {
			container = new PostgreSQLContainer(POSTGRES_IMAGE)
					.withDatabaseName(DATABASE_NAME)
					.withUsername(DB_USERNAME)
					.withPassword(DB_PASSWORD);
			container.start();
			
			ContainerRegistry.registerContainerAsRunning(POSTGRESQL, container);
		}
	}
	
	private static TestTemplateInvocationContext createTestTemplateContext() {
		return new TestTemplateInvocationContext() {
			
			@Override
			public String getDisplayName(int invocationIndex) {
				return "PostgreSQL";
			}
			
			@Override
			public List<Extension> getAdditionalExtensions() {
				return createExtensions();
			}
		};
	}
	
	private static List<Extension> createExtensions() {
		final List<Extension> extensions = new ArrayList<>();
		
		extensions.add(createInvocationInterceptor());
		extensions.add(createAfterAll());
		
		return extensions;
	}
	
	private static Extension createInvocationInterceptor() {
		return new InvocationInterceptor() {
			@Override
			public <T> T interceptTestClassConstructor(final Invocation<T> invocation,
			                                           final ReflectiveInvocationContext<Constructor<T>> invocationContext,
			                                           final ExtensionContext extensionContext) throws Throwable {
				TestContainerProperties.setCurrentProfile(PROFILE, container);
				System.setProperty("POSTGRE_HOST", container.getJdbcUrl() + "&currentSchema=x_scraper");
				
				return InvocationInterceptor.super.interceptTestClassConstructor(invocation, invocationContext, extensionContext);
			}
		};
	}
	
	private static Extension createAfterAll() {
		return (AfterAllCallback) context -> {
//			container.stop();
		};
	}
}
