package com.x.scrape.storage;

import com.x.scrape.logging.CloseableContext;
import com.x.scrape.logging.ContextLogger;
import com.x.scrape.model.task.event.task_result.StorageHint;
import com.x.scrape.model.task.event.task_result.TaskResultEvent;
import com.x.scrape.model.event.storable.payload.Payload;
import com.x.scrape.model.event.storable.payload.ImagePayload;
import com.x.scrape.model.event.storable.payload.MapPayload;
import com.x.scrape.model.event.storable.payload.StringPayload;
import com.x.scrape.storage.io.FileWritingService;
import com.x.scrape.storage.json.JsonService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.x.scrape.model.task.event.task_result.StorageType.*;

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
	 * @param event the event holding the result and {@link StorageHint} that says where the result should be stored.
	 */
	@EventListener
	@Async
	public void onTaskResultEvent(final TaskResultEvent event) {
		try (final CloseableContext ignored = logger.with(event)) {
			logger.info("Saving task result.");
			saveResult(event.getStorageHint(), event.getPayload());
		}
	}
	
	private void saveResult(final StorageHint storageHint,
	                        final Payload<?> payload) {
		if (storageHint.getType() == JSON) {
			saveJsonResult(storageHint, payload);
		} else if (storageHint.getType() == IMAGE) {
			saveImageResult(storageHint, payload);
		} else if (storageHint.getType() == RAW) {
			saveRawResult(storageHint, payload);
		}
	}
	
	private void saveJsonResult(final StorageHint storageHint,
	                            final Payload<?> payload) {
		if (payload instanceof MapPayload mapPayload) {
			fileWritingService.write(
					storageHint.getFolder(),
					storageHint.getFileName(),
					jsonService.toJson(mapPayload.getData())
			);
		} else {
			throw new IllegalArgumentException("Unsupported payload type " + payload.getClass().getSimpleName() + " for storage type " + storageHint.getType().name());
		}
	}
	
	private void saveImageResult(final StorageHint storageHint,
	                             final Payload<?> payload) {
		if (payload instanceof ImagePayload imagePayload) {
			fileWritingService.write(
					storageHint.getFolder(),
					storageHint.getFileName(),
					imagePayload.getData()
			);
		} else {
			throw new IllegalArgumentException("Unsupported payload type " + payload.getClass().getSimpleName() + " for storage type " + storageHint.getType().name());
		}
	}
	
	private void saveRawResult(final StorageHint storageHint,
	                           final Payload<?> payload) {
		if (payload instanceof StringPayload stringPayload) {
			fileWritingService.write(
					storageHint.getFolder(),
					storageHint.getFileName(),
					stringPayload.getData()
			);
		} else {
			throw new IllegalArgumentException("Unsupported payload type " + payload.getClass().getSimpleName() + " for storage type " + storageHint.getType().name());
		}
	}
}
