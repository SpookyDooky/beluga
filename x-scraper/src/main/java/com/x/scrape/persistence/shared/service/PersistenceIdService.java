package com.x.scrape.persistence.shared.service;

import com.x.scrape.persistence.shared.event.SequenceIncrementedEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.concurrent.atomic.AtomicLong;

public class PersistenceIdService {
	
	private final AtomicLong sequence;
	private final ApplicationEventPublisher applicationEventPublisher;
	
	public PersistenceIdService(final Long sequence,
	                            final ApplicationEventPublisher applicationEventPublisher) {
		this.sequence = new AtomicLong(sequence);
		this.applicationEventPublisher = applicationEventPublisher;
	}
	
	/**
	 * Thread-safe get method for obtaining the next persistence id.
	 *
	 * @return the next id for persistence.
	 */
	public Long getNext() {
		synchronized (sequence) {
			final Long next =  sequence.getAndIncrement();
			applicationEventPublisher.publishEvent(new SequenceIncrementedEvent(next));
			return next;
		}
	}
}
