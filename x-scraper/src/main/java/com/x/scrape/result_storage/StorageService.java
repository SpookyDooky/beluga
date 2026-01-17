package com.x.scrape.result_storage;

import com.x.scrape.logging.CloseableContext;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.model.event.storable.StorableEvent;
import com.x.scrape.model.event.storable.payload.ImagePayload;
import com.x.scrape.model.event.storable.payload.JsonPayload;
import com.x.scrape.model.event.storable.payload.Payload;
import com.x.scrape.model.event.storable.payload.StringPayload;
import com.x.scrape.model.task.event.task_result.StorageHint;
import com.x.scrape.result_storage.json.JsonService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class StorageService {
	
	private final ContextLogger logger;
	private final JsonService jsonService;
	private final ResultDataStoreProvider resultDataStoreProvider;
	
	public StorageService(final ContextLogger logger,
	                      final JsonService jsonService,
	                      final ResultDataStoreProvider resultDataStoreProvider) {
		this.logger = logger;
		this.jsonService = jsonService;
		this.resultDataStoreProvider = resultDataStoreProvider;
	}
	
	/**
	 * Asynchronously stores the result from a task in the correct location.
	 *
	 * @param event the event holding the result and {@link StorageHint} that says where the result should be stored.
	 */
	@EventListener
	@Async
	public void onTaskResultEvent(final StorableEvent event) {
		try (final CloseableContext ignored = logger.with(event)) {
			logger.info("Saving task result.");
			saveResult(event.getStorageHint(), event.getPayload());
		}
	}
	
	private void saveResult(final StorageHint storageHint,
	                        final Payload<?> payload) {
		switch (payload) {
			case JsonPayload jsonPayload -> saveJsonResult(storageHint, jsonPayload);
			case ImagePayload imagePayload -> saveImageResult(storageHint, imagePayload);
			case StringPayload stringPayload -> saveRawResult(storageHint, stringPayload);
			default -> throw new IllegalStateException("Unsupported payload type: " + payload.getClass().getSimpleName());
		}
	}
	
	private void saveJsonResult(final StorageHint storageHint,
	                            final JsonPayload payload) {
		resultDataStoreProvider.save(
				storageHint.getPath(),
				jsonService.toJson(payload.getData()).getBytes()
		);
		
	}
	
	private void saveImageResult(final StorageHint storageHint,
	                             final ImagePayload imagePayload) {
		resultDataStoreProvider.save(
				storageHint.getPath(),
				imagePayload.getData()
		);
	}
	
	private void saveRawResult(final StorageHint storageHint,
	                           final StringPayload stringPayload) {
		resultDataStoreProvider.save(
				storageHint.getPath(),
				stringPayload.getData().getBytes()
		);
	}
	
	public byte[] retrieve(final Path filePath) {
		return resultDataStoreProvider.retrieve(filePath);
	}
}
