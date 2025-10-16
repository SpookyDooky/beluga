package com.x.scrape.model.event.storable.payload;

import java.util.List;
import java.util.Map;

public class MapPayload extends Payload<List<Map<String, Object>>> {
	public MapPayload(final List<Map<String, Object>> data) {
		super(data);
	}
}
