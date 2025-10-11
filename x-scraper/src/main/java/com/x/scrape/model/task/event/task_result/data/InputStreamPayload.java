package com.x.scrape.model.task.event.task_result.data;

import java.io.InputStream;

public class InputStreamPayload extends DataPayload<InputStream> {
	public InputStreamPayload(final InputStream data) {
		super(data);
	}
}
