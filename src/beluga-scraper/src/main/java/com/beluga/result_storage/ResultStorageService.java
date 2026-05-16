package com.beluga.result_storage;

import com.beluga.execution.event.task.task_result.StorageHint;
import com.beluga.execution.event.task.task_result.TaskResultEvent;
import com.beluga.logging.CloseableContext;
import com.beluga.logging.ContextLogger;
import com.beluga.model.event.storable.payload.ImagePayload;
import com.beluga.model.event.storable.payload.JsonPayload;
import com.beluga.model.event.storable.payload.Payload;
import com.beluga.model.event.storable.payload.StringPayload;
import com.beluga.result_storage.json.JsonService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
			final String resourceIdentifier = namespace + "/" + event.getKey();

			saveResult(
					resourceIdentifier,
					event.getPayload()
			);
		}
	}
	
	private void saveResult(final String resourceIdentifier,
	                        final Payload<?> payload) {
		switch (payload) {
			case JsonPayload jsonPayload -> saveJsonResult(resourceIdentifier, jsonPayload);
			case ImagePayload imagePayload -> saveImageResult(resourceIdentifier, imagePayload);
			case StringPayload stringPayload -> saveRawResult(resourceIdentifier, stringPayload);
			default -> throw new IllegalStateException("Unsupported payload type: " + payload.getClass().getSimpleName());
		}
	}
	
	private void saveJsonResult(final String resourceIdentifier,
	                            final JsonPayload payload) {
		resultDataStoreProvider.save(
				resourceIdentifier,
				jsonService.toJson(payload.getData()).getBytes()
		);
		
	}
	
	private void saveImageResult(final String resourceIdentifier,
	                             final ImagePayload imagePayload) {
		resultDataStoreProvider.save(
				resourceIdentifier,
				imagePayload.getData()
		);
	}
	
	private void saveRawResult(final String resourceIdentifier,
	                           final StringPayload stringPayload) {
		resultDataStoreProvider.save(
				resourceIdentifier,
				stringPayload.getData().getBytes()
		);
	}
	
	public byte[] retrieve(final String resourceIdentifier) {
		return resultDataStoreProvider.retrieve(resourceIdentifier);
	}
}
