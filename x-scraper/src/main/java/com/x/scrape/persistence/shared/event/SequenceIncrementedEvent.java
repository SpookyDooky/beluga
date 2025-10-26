package com.x.scrape.persistence.shared.event;

import org.springframework.context.ApplicationEvent;

/**
 * Event that is published when the persistence sequence is incremented.
 */
public class SequenceIncrementedEvent extends ApplicationEvent {
	
	private final Long sequence;
	
	public SequenceIncrementedEvent(final Long sequence) {
		super(sequence);
		this.sequence = sequence;
	}
	
	public Long getSequence() {
		return sequence;
	}
}
