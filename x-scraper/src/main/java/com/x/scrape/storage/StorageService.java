package com.x.scrape.storage;

import com.x.scrape.logging.CloseableContext;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.model.event.storable.StorableEvent;
import com.x.scrape.model.event.storable.payload.ImagePayload;
import com.x.scrape.model.event.storable.payload.JsonPayload;
import com.x.scrape.model.event.storable.payload.Payload;
import com.x.scrape.model.event.storable.payload.StringPayload;
import com.x.scrape.model.task.event.task_result.StorageHint;
import com.x.scrape.storage.io.FileWritingService;
import com.x.scrape.storage.json.JsonService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class StorageService {
	
	private final ContextLogger logger;
	private final JsonService jsonService;
	private final FileWritingService fileWritingService;
	
	public StorageService(final ContextLogger logger,
	                      final JsonService jsonService,
	                      final FileWritingService fileWritingService) {
		this.logger = logger;
		this.jsonService = jsonService;
		this.fileWritingService = fileWritingService;
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
		fileWritingService.write(
				storageHint.getFolder(),
				storageHint.getFileName(),
				jsonService.toJson(payload.getData())
		);
		
	}
	
	private void saveImageResult(final StorageHint storageHint,
	                             final ImagePayload imagePayload) {
		fileWritingService.write(
				storageHint.getFolder(),
				storageHint.getFileName(),
				imagePayload.getData()
		);
	}
	
	private void saveRawResult(final StorageHint storageHint,
	                           final StringPayload stringPayload) {
		fileWritingService.write(
				storageHint.getFolder(),
				storageHint.getFileName(),
				stringPayload.getData()
		);
	}
}
