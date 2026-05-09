package com.beluga.api.results.mapper;

import com.beluga.api.results.dto.ResultFileInfoDto;
import com.beluga.model.result.ResultFile;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ResultFileInfoMapper {
	
	ResultFileInfoDto map(ResultFile resultFile);
}
