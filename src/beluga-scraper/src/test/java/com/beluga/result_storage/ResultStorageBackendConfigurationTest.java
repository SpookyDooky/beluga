package com.beluga.result_storage;

import com.beluga.logging.config.LoggingConfig;
import com.beluga.properties.BelugaScraperProperties;
import com.beluga.result_storage.file_system.FileSystemResultDataStoreProvider;
import com.beluga.result_storage.json.JsonService;
import com.beluga.result_storage.s3.config.S3Config;
import com.beluga.result_storage.s3.service.S3ResultDataStoreProvider;
import com.beluga.result_storage.s3.service.S3Service;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Verifies that the result storage backend selected by {@code beluga.result-storage.type} is wired into
 * {@link ResultStorageService}. Loads the real result storage beans with their real conditions; only {@link JsonService}
 * is mocked because it is unrelated to backend selection.
 */
class ResultStorageBackendConfigurationTest {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withUserConfiguration(
					TestConfig.class,
					LoggingConfig.class,
					NamespaceFactory.class,
					ResultStorageService.class,
					FileSystemResultDataStoreProvider.class,
					S3Config.class,
					S3Service.class,
					S3ResultDataStoreProvider.class
			)
			.withBean(JsonService.class, () -> mock(JsonService.class))
			.withPropertyValues("beluga.persistence.type=SQLITE");

	@Test
	void shouldStartWithFileSystemResultStorage() {
		contextRunner.withPropertyValues(
				"beluga.result-storage.type=FILE_SYSTEM"
		).run(context -> {
			assertThat(context).hasNotFailed();
			assertThat(context).hasSingleBean(ResultStorageService.class);
			assertThat(context).hasSingleBean(FileSystemResultDataStoreProvider.class);
			assertThat(context).doesNotHaveBean(S3ResultDataStoreProvider.class);
		});
	}

	@Test
	void shouldStartWithS3ResultStorage() {
		contextRunner.withPropertyValues(
				"beluga.result-storage.type=S3",
				"beluga.result-storage.s3.host=http://localhost:9000",
				"beluga.result-storage.s3.access-key=access-key",
				"beluga.result-storage.s3.secret-key=secret-key",
				"beluga.result-storage.s3.bucket=bucket",
				"beluga.result-storage.s3.region=us-east-1"
		).run(context -> {
			assertThat(context).hasNotFailed();
			assertThat(context).hasSingleBean(ResultStorageService.class);
			assertThat(context).hasSingleBean(S3ResultDataStoreProvider.class);
			assertThat(context).hasSingleBean(S3Service.class);
			assertThat(context).doesNotHaveBean(FileSystemResultDataStoreProvider.class);
		});
	}

	@EnableConfigurationProperties(BelugaScraperProperties.class)
	static class TestConfig {

	}
}
