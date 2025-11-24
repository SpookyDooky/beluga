package com.x.scrape.integration_test.datastore;

import com.x.scrape.integration_test.properties.TestContainerProperties;
import org.junit.jupiter.api.extension.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MinIOContainer;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static software.amazon.awssdk.regions.Region.US_EAST_1;

public class S3TemplateFactory {
	
	private static final String PROFILE = "s3";
	
	public static TestTemplateInvocationContext createContext(final GenericContainer<?> container) {
		if (container instanceof MinIOContainer minIOContainer) {
			return createContext(minIOContainer);
		}
		
		throw new IllegalArgumentException("Unsupported container: " + container.getClass().getSimpleName());
	}
	
	private static TestTemplateInvocationContext createContext(final MinIOContainer container) {
		return new TestTemplateInvocationContext() {
			
			@Override
			public String getDisplayName(int invocationIndex) {
				return "S3";
			}
			
			@Override
			public List<Extension> getAdditionalExtensions() {
				return createExtensions(container);
			}
		};
	}
	
	private static List<Extension> createExtensions(final MinIOContainer container) {
		final List<Extension> extensions = new ArrayList<>();
		
		extensions.add(createInvocationInterceptor(container));
		extensions.add(createAfterAll(container));
		
		return extensions;
	}
	
	private static Extension createInvocationInterceptor(final MinIOContainer container) {
		return new InvocationInterceptor() {
			@Override
			public <T> T interceptTestClassConstructor(final Invocation<T> invocation, final ReflectiveInvocationContext<Constructor<T>> invocationContext, final ExtensionContext extensionContext) throws Throwable {
				TestContainerProperties.setCurrentProfile(PROFILE, container);
				container.start();
				
				createTestBucket(container);
				System.setProperty("S3_HOST", container.getS3URL());
				
				return InvocationInterceptor.super.interceptTestClassConstructor(invocation, invocationContext, extensionContext);
			}
			
		};
	}
	
	private static void createTestBucket(final MinIOContainer container) {
		final AwsBasicCredentials credentials = AwsBasicCredentials.create("username", "password");
		
		final S3Client s3Client = S3Client.builder()
				.endpointOverride(URI.create(container.getS3URL()))
				.credentialsProvider(StaticCredentialsProvider.create(credentials))
				.region(US_EAST_1)
				.forcePathStyle(true)
				.build();
		
		createTestBucket(s3Client);
	}
	
	private static void createTestBucket(final S3Client s3Client) {
		final CreateBucketRequest request = CreateBucketRequest.builder()
				.bucket("test")
				.build();
		
		s3Client.createBucket(request);
	}
	
	private static Extension createAfterAll(final MinIOContainer container) {
		return (AfterAllCallback) context -> {
			container.stop();
		};
	}
	
}
