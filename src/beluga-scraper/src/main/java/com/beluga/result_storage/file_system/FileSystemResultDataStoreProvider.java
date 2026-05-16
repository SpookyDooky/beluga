package com.beluga.result_storage.file_system;

import com.beluga.logging.CloseableContext;
import com.beluga.logging.ContextLogger;
import com.beluga.result_storage.ResultDataStoreProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.beluga.logging.ContextKeys.FILE_NAME;

@Service
// TODO MAKE custom annotation @IsResultStoreFileSystem
@ConditionalOnProperty(
		name = "beluga.result-datastore.type",
		havingValue = "FILE_SYSTEM"
)
public class FileSystemResultDataStoreProvider extends ResultDataStoreProvider {
	
	public FileSystemResultDataStoreProvider(final ContextLogger logger) {
		super(logger);
	}
	
	@Override
	public void save(final String resourceIdentifier,
	                 final byte[] fileContent) {
		final Path filePath = Path.of(resourceIdentifier);

		try (final CloseableContext ignored = logger.with(FILE_NAME, filePath.getFileName().toString())) {
			final File file = filePath.toFile();
			write(file, fileContent);
		}
	}
	
	private void write(final File file,
	                   final byte[] bytes) {
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		
		try (final FileOutputStream fileOutputStream = new FileOutputStream(file)){
			fileOutputStream.write(bytes);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public byte[] retrieve(final String resourceIdentifier) {
		try {
			return Files.readAllBytes(Path.of(resourceIdentifier));
		} catch (final IOException e) {
			throw new IllegalStateException("Could not read file", e);
		}
	}
}
