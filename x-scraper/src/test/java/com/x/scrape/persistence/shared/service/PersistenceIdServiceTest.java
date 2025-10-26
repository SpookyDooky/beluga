package com.x.scrape.persistence.shared.service;

import com.x.scrape.persistence.shared.event.SequenceIncrementedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PersistenceIdServiceTest {
	
	@Mock
	private ApplicationEventPublisher applicationEventPublisher;
	
	private PersistenceIdService persistenceIdService;
	
	@Captor
	private ArgumentCaptor<SequenceIncrementedEvent> eventArgumentCaptor;
	
	@BeforeEach
	void setup() {
		persistenceIdService = new PersistenceIdService(0L, applicationEventPublisher);
	}
	
	@Test
	void shouldGetNext() {
		assertEquals(0, persistenceIdService.getNext());
		assertEquals(1, persistenceIdService.getNext());
		
		verify(applicationEventPublisher, times(2)).publishEvent(eventArgumentCaptor.capture());
		
		final List<SequenceIncrementedEvent> events = eventArgumentCaptor.getAllValues();
		assertEquals(0, events.get(0).getSequence());
		assertEquals(1, events.get(1).getSequence());
	}
	
	@Test
	void shouldGetNextWithMultipleThreads() throws Exception {
		final List<Thread> threads = new ArrayList<>();
		final List<Long> sequences = new ArrayList<>();
		
		for (int i = 0; i < 1000; i++) {
			threads.add(
					new Thread(() -> {
						final Long next = persistenceIdService.getNext();
						sequences.add(next);
					})
			);
		}
		
		threads.forEach(Thread::start);
		Thread.sleep(2000);
		
		final List<Long> copy = new ArrayList<>(sequences);
		Collections.sort(copy);
		
		assertEquals(sequences, copy);
	}
}