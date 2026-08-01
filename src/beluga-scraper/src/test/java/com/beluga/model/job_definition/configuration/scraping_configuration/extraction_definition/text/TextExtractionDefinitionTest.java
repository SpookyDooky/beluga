package com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.text;

import com.beluga.test_utils.TestReflectionUtility;
import jakarta.persistence.DiscriminatorValue;
import org.junit.jupiter.api.Test;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.TEXT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TextExtractionDefinitionTest {

    @Test
    void shouldHaveCorrectType() {
        final TextExtractionDefinition definition = new TextExtractionDefinition();
        assertEquals(TEXT, definition.getType());
    }

    @Test
    void shouldHaveCorrectDiscriminatorValue() {
        final DiscriminatorValue discriminatorValue = TestReflectionUtility.assertAnnotationPresentOnClass(
                TextExtractionDefinition.class,
                DiscriminatorValue.class
        );

        assertEquals("TEXT", discriminatorValue.value());
    }
}