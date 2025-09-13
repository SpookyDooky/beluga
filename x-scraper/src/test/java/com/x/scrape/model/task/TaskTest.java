package com.x.scrape.model.task;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
	
	@Test
	void shouldCreateUniqueId() {
		final UUID id1 = new Task().getId();
		final UUID id2 = new Task().getId();
		
		assertNotEquals(id1, id2);
	}
}