package com.x.scrape.api.results.mapper;

import com.x.scrape.api.results.dto.ResultFileInfoDto;
import com.x.scrape.model.result.ResultFile;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultFileInfoMapperTest {
	
	private final ResultFileInfoMapper mapper = new ResultFileInfoMapperImpl();
	
	@Test
	void shouldMap() {
		final ResultFile resultFile = Instancio.create(ResultFile.class);
		
		final ResultFileInfoDto result = mapper.map(resultFile);
		
		assertEquals(resultFile.getId(), result.getId());
		assertEquals(resultFile.getFileName(), result.getFileName());
		assertEquals(resultFile.getPath(), result.getPath());
		assertEquals(resultFile.getSizeInBytes(), result.getSizeInBytes());
	}
}