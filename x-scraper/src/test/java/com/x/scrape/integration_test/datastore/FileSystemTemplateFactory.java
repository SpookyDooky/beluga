package com.x.scrape.integration_test.datastore;

import com.x.scrape.integration_test.properties.TestContainerProperties;
import org.junit.jupiter.api.extension.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class FileSystemTemplateFactory {
	
	private static final String PROFILE = "filesystem";
	
	public static TestTemplateInvocationContext createContext() {
		return new TestTemplateInvocationContext() {
			@Override
			public String getDisplayName(final int invocationIndex) {
				return "filesystem";
			}
			
			@Override
			public List<Extension> getAdditionalExtensions() {
				return createExtensions();
			}
		};
	}
	
	private static List<Extension> createExtensions() {
		final List<Extension> extensions = new ArrayList<>();
		
		final Path tempDirectory = createTempDirectory();
		
		extensions.add(createInvocationInterceptor(tempDirectory));
		extensions.add(createAfterAllExtension(tempDirectory));
		
		return extensions;
	}
	
	private static Path createTempDirectory() {
		try {
			return Files.createTempDirectory(UUID.randomUUID().toString());
		} catch (final IOException e) {
			throw new IllegalStateException("Failed to create temporary directory", e);
		}
	}
	
	private static Extension createInvocationInterceptor(final Path tempDirectory) {
		return new InvocationInterceptor() {
			@Override
			public <T> T interceptTestClassConstructor(final Invocation<T> invocation,
			                                           final ReflectiveInvocationContext<Constructor<T>> invocationContext,
			                                           final ExtensionContext extensionContext) throws Throwable {
				TestContainerProperties.setCurrentProfile(PROFILE);
				
				System.setProperty("PERSISTENCE_FOLDER", tempDirectory.toString());
				return InvocationInterceptor.super.interceptTestClassConstructor(invocation, invocationContext, extensionContext);
			}
		};
	}
	
	private static Extension createAfterAllExtension(final Path tempDirectory) {
		return (AfterAllCallback) context -> {
			Files.walk(tempDirectory)
					.sorted(Comparator.reverseOrder())
					.forEach(path -> {
						try {
							Files.delete(path);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					});
		};
	}
}
