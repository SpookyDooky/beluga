package com.x.scrape.api.job.dto.read;

import com.x.scrape.api.job.dto.base.BaseScrapingDataPointDto;

public class ReadDataPointConfigurationDto extends BaseScrapingDataPointDto {
	
	private Long id;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
}
