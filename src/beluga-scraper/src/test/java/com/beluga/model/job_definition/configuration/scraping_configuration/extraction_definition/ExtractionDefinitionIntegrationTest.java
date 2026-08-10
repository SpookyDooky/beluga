package com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition;

import com.beluga.integration_test.BaseIntegrationTest;
import com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.text.TextExtractionDefinition;
import com.beluga.repository.ExtractionDefinitionRepository;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExtractionDefinitionIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ExtractionDefinitionRepository extractionDefinitionRepository;

    @TestTemplate
    void shouldSaveExtractionDefinition() {
        final TextExtractionDefinition textExtractionDefinition = new TextExtractionDefinition();
        textExtractionDefinition.setField("some_field");

        extractionDefinitionRepository.save(textExtractionDefinition);

        assertTrue(extractionDefinitionRepository.findById(textExtractionDefinition.getId()).isPresent());
    }
}
