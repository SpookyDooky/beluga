package com.x.scrape.integration_test.datastore;

import com.x.scrape.integration_test.ContainerRegistry;
import com.x.scrape.integration_test.properties.TestContainerProperties;
import org.junit.jupiter.api.extension.*;
import org.testcontainers.containers.MinIOContainer;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.x.scrape.properties.persistence.PersistenceType.S3;
import static software.amazon.awssdk.regions.Region.US_EAST_1;

public class S3TemplateFactory {
	
	private static final String MINIO_IMAGE = "minio/minio:latest";
	
	private static final String MINIO_USERNAME = "username";
	private static final String MINIO_PASSWORD = "password";
	
	private static final String PROFILE = "s3";
	
	private static MinIOContainer container;
	
	public static TestTemplateInvocationContext createContext() {
		startContainer();
		return createTestTemplateContext();
	}
	
	private static void startContainer() {
		if (!ContainerRegistry.isContainerRunning(S3)) {
			container = new MinIOContainer(MINIO_IMAGE)
					.withUserName(MINIO_USERNAME)
					.withPassword(MINIO_PASSWORD);
			container.start();
			
			createTestBucket();
			
			ContainerRegistry.registerContainerAsRunning(S3, container);
		}
	}
	
	private static void createTestBucket() {
		final AwsBasicCredentials credentials = AwsBasicCredentials.create(MINIO_USERNAME, MINIO_PASSWORD);
		
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
	
	private static TestTemplateInvocationContext createTestTemplateContext() {
		return new TestTemplateInvocationContext() {
			
			@Override
			public String getDisplayName(int invocationIndex) {
				return "S3";
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
			public <T> T interceptTestClassConstructor(final Invocation<T> invocation, final ReflectiveInvocationContext<Constructor<T>> invocationContext, final ExtensionContext extensionContext) throws Throwable {
				TestContainerProperties.setCurrentProfile(PROFILE, container);
				System.setProperty("S3_HOST", container.getS3URL());
				
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
