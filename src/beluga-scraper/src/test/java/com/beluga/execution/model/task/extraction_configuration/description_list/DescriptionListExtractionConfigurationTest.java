package com.beluga.execution.model.task.extraction_configuration.description_list;

import org.junit.jupiter.api.Test;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.DESCRIPTION_LIST;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DescriptionListExtractionConfigurationTest {

    @Test
    void shouldHaveCorrectType() {
        final DescriptionListExtractionConfiguration configuration = new DescriptionListExtractionConfiguration();

        assertEquals(DESCRIPTION_LIST, configuration.getType());
    }
}