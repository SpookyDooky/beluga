package com.x.scrape.api.results.mapper;

import com.x.scrape.api.results.dto.ResultFileInfoDto;
import com.x.scrape.model.result.ResultFile;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ResultFileInfoMapper {
	
	ResultFileInfoDto map(ResultFile resultFile);
}
