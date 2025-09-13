package com.x.scrape.model.job;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JobTest {
	
	@Test
	void shouldCreateUniqueId() {
		final UUID id1 = new Job().getId();
		final UUID id2 = new Job().getId();
		
		assertNotEquals(id1, id2);
	}
}