package com.x.scrape.persistence.shared.service;

import com.x.scrape.persistence.shared.event.SequenceIncrementedEvent;

public interface StateService {
	
	void onSequenceIncremented(SequenceIncrementedEvent event);
}
