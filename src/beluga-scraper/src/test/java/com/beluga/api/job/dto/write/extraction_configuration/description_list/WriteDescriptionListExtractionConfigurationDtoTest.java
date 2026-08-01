package com.beluga.api.job.dto.write.extraction_configuration.description_list;

import org.junit.jupiter.api.Test;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.DESCRIPTION_LIST;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WriteDescriptionListExtractionConfigurationDtoTest {

    @Test
    void shouldHaveCorrectType() {
        final WriteDescriptionListExtractionConfigurationDto dto = new WriteDescriptionListExtractionConfigurationDto();
        assertEquals(DESCRIPTION_LIST, dto.getType());
    }
}