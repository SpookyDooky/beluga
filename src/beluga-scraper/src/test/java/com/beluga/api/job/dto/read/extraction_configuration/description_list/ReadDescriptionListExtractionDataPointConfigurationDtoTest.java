package com.beluga.api.job.dto.read.extraction_configuration.description_list;

import org.junit.jupiter.api.Test;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.DESCRIPTION_LIST;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReadDescriptionListExtractionDataPointConfigurationDtoTest {

    @Test
    void shouldHaveCorrectType() {
        final ReadDescriptionListExtractionConfigurationDto dto = new ReadDescriptionListExtractionConfigurationDto();

        assertEquals(DESCRIPTION_LIST, dto.getType());
    }
}