package com.x.scrape.model.task.event.task_result.data;

import java.util.Map;

public class MapPayload extends DataPayload<Map<String, Object>> {
	public MapPayload(final Map<String, Object> data) {
		super(data);
	}
}
