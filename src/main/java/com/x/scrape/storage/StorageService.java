package com.x.scrape.storage;

import com.x.scrape.model.job.storage.StorageConfiguration;
import com.x.scrape.storage.io.FileWritingService;
import com.x.scrape.storage.json.JsonService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StorageService {
	
	private final JsonService jsonService;
	private final FileWritingService fileWritingService;
	
	public StorageService(final JsonService jsonService,
	                      final FileWritingService fileWritingService) {
		this.jsonService = jsonService;
		this.fileWritingService = fileWritingService;
	}
	
	public void saveContent(final StorageConfiguration storageConfiguration,
	                        final List<Map<String, Object>> content) {
		final String jsonContent = jsonService.toJson(content);
		
		fileWritingService.write(
				storageConfiguration.getFolder(),
				storageConfiguration.getFile(),
				jsonContent
		);
	}
}
