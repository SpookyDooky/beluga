package com.beluga.api.results.mapper;

import com.beluga.api.results.dto.ResultFileInfoDto;
import com.beluga.model.result.ResultFile;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResultFileInfoMapperTest {
	
	private final ResultFileInfoMapper mapper = new ResultFileInfoMapperImpl();
	
	@Test
	void shouldMap() {
		final ResultFile resultFile = Instancio.create(ResultFile.class);
		
		final ResultFileInfoDto result = mapper.map(resultFile);
		
		assertEquals(resultFile.getId(), result.getId());
		assertEquals(resultFile.getResourceIdentifier(), result.getResourceIdentifier());
		assertEquals(resultFile.getSizeInBytes(), result.getSizeInBytes());
	}
}