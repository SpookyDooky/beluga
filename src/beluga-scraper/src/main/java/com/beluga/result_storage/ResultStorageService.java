package com.beluga.result_storage;

import com.beluga.execution.event.task.task_result.TaskResultEvent;
import com.beluga.logging.CloseableContext;
import com.beluga.logging.ContextLogger;
import com.beluga.model.event.storable.StorableEvent;
import com.beluga.model.event.storable.payload.ImagePayload;
import com.beluga.model.event.storable.payload.JsonPayload;
import com.beluga.model.event.storable.payload.Payload;
import com.beluga.model.event.storable.payload.StringPayload;
import com.beluga.execution.event.task.task_result.StorageHint;
import com.beluga.result_storage.json.JsonService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.xml.stream.events.Namespace;
import java.nio.file.Path;

@Service
public class ResultStorageService {
	
	private final ContextLogger logger;
	private final JsonService jsonService;
	private final NamespaceFactory namespaceFactory;
	private final ResultDataStoreProvider resultDataStoreProvider;
	private final ApplicationEventPublisher eventPublisher;

	public ResultStorageService(final ContextLogger logger,
	                            final JsonService jsonService,
	                            final NamespaceFactory namespaceFactory,
	                            final ResultDataStoreProvider resultDataStoreProvider,
								final ApplicationEventPublisher applicationEventPublisher) {
		this.logger = logger;
		this.jsonService = jsonService;
		this.namespaceFactory = namespaceFactory;
		this.resultDataStoreProvider = resultDataStoreProvider;
		this.eventPublisher = applicationEventPublisher;
	}
	
	/**
	 * Asynchronously stores the result from a task in the correct location.
	 *
	 * @param event the event holding the result and {@link StorageHint} that says where the result should be stored.
	 */
	@EventListener
	@Async
	public void onTaskResultEvent(final TaskResultEvent event) {
		try (final CloseableContext ignored = logger.with(event)) {
			logger.info("Saving task result.");

			final String namespace = namespaceFactory.create(event);

			saveResult(event.getStorageHint(), event.getPayload());
		}
	}
	
	private void saveResult(f,
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
