package com.x.scrape.integration_test.datastore;

import com.x.scrape.integration_test.properties.TestContainerProperties;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class SqliteTemplateFactory {
	
	private static final String PROFILE = "sqlite";
	
	public static TestTemplateInvocationContext createContext() {
		return createTestTemplateContext();
	}
	
	private static TestTemplateInvocationContext createTestTemplateContext() {
		return new TestTemplateInvocationContext() {
			
			@Override
			public String getDisplayName(int invocationIndex) {
				return "SQLite";
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
		
		return extensions;
	}
	
	private static Extension createInvocationInterceptor() {
		return new InvocationInterceptor() {
			@Override
			public <T> T interceptTestClassConstructor(final Invocation<T> invocation,
			                                           final ReflectiveInvocationContext<Constructor<T>> invocationContext,
			                                           final ExtensionContext extensionContext) throws Throwable {
				TestContainerProperties.setCurrentProfile(PROFILE, null);
				return InvocationInterceptor.super.interceptTestClassConstructor(invocation, invocationContext, extensionContext);
			}
		};
	}
}
