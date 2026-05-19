package com.beluga.result_storage;

import com.beluga.execution.event.task.task_result.TaskResultEvent;
import com.beluga.logging.ContextLogger;
import com.beluga.model.event.storable.payload.ImagePayload;
import com.beluga.model.event.storable.payload.JsonPayload;
import com.beluga.model.event.storable.payload.Payload;
import com.beluga.result_storage.json.JsonService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.io.ByteArrayInputStream;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResultStorageServiceTest {
	
	@Mock
	private ContextLogger logger;
	@Mock
	private JsonService jsonService;
	@Mock
	private NamespaceFactory namespaceFactory;
	@Mock
	private ResultDataStoreProvider resultDataStoreProvider;
	@Mock
	private ApplicationEventPublisher eventPublisher;

	@InjectMocks
	private ResultStorageService resultStorageService;
	
	@Test
	void shouldSaveJsonResult() {
		final TaskResultEvent jsonTaskResultEvent = createTaskResultEvent(JsonPayload.class);
		
		final String json = "json";
		final JsonPayload mapPayload = (JsonPayload) jsonTaskResultEvent.getPayload();
		when(jsonService.toJson(mapPayload.getData())).thenReturn(json);

		final String namespace = "namespace";
		when(namespaceFactory.create(jsonTaskResultEvent)).thenReturn(namespace);

		resultStorageService.onTaskResultEvent(jsonTaskResultEvent);
		
		verify(resultDataStoreProvider).save(
				namespace + "/" + jsonTaskResultEvent.getKey(),
				json.getBytes()
		);
	}
	
	TaskResultEvent createTaskResultEvent(final Class<? extends Payload> payloadClass) {
		return Instancio.of(TaskResultEvent.class)
				.set(field(TaskResultEvent::getPayload), Instancio.create(payloadClass))
				.create();
	}
	
	@Test
	void shouldSaveInputStreamResult() {
		final TaskResultEvent imageTaskResultEvent = createTaskResultEvent(ImagePayload.class);
		final ImagePayload imagePayload = (ImagePayload) imageTaskResultEvent.getPayload();

		final String namespace = "namespace";
		when(namespaceFactory.create(imageTaskResultEvent)).thenReturn(namespace);

		resultStorageService.onTaskResultEvent(imageTaskResultEvent);
		
		verify(resultDataStoreProvider).save(
				eq(namespace + "/" + imageTaskResultEvent.getKey()),
				any(ByteArrayInputStream.class)
		);
	}
	
	@Test
	void shouldRetrieve() {
		final byte[] expected = new byte[0];
		when(resultDataStoreProvider.retrieve("")).thenReturn(expected);
		
		final byte[] result = resultStorageService.retrieve("");
		
		assertSame(expected, result);
	}
}