package com.x.scrape.model.task.event.task_result.data;

import java.util.List;
import java.util.Map;

public class MapPayload extends DataPayload<List<Map<String, Object>>> {
	public MapPayload(final List<Map<String, Object>> data) {
		super(data);
	}
}
