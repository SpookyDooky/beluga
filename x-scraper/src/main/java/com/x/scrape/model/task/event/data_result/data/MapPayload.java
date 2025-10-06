package com.x.scrape.model.task.event.data_result.data;

import java.util.Map;

public class MapPayload extends DataPayload<Map<String, Object>> {
	public MapPayload(final Map<String, Object> data) {
		super(data);
	}
}
