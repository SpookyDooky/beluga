package com.x.scrape.persistence.sql_lite.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Instant;

@Converter(autoApply = true)
public class InstantConverter implements AttributeConverter<Instant, String> {
	
	@Override
	public String convertToDatabaseColumn(final Instant instant) {
		if (instant == null) {
			return null;
		}
		
		return instant.toString();
	}
	
	@Override
	public Instant convertToEntityAttribute(final String dbData) {
		if (dbData == null) {
			return null;
		}
		
		return Instant.parse(dbData);
	}
}
