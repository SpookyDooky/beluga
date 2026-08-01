package com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.html;

import com.beluga.test_utils.TestReflectionUtility;
import jakarta.persistence.DiscriminatorValue;
import org.junit.jupiter.api.Test;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.HTML;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HtmlExtractionDefinitionTest {

    @Test
    void shouldHaveCorrectType() {
        final HtmlExtractionDefinition definition = new HtmlExtractionDefinition();
        assertEquals(HTML, definition.getType());
    }

    @Test
    void shouldHaveCorrectDiscriminatorValue() {
        final DiscriminatorValue discriminatorValue = TestReflectionUtility.assertAnnotationPresentOnClass(
                HtmlExtractionDefinition.class,
                DiscriminatorValue.class
        );

        assertEquals("HTML", discriminatorValue.value());
    }
}