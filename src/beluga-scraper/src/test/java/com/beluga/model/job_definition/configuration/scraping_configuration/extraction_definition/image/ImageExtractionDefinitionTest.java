package com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.image;

import com.beluga.test_utils.TestReflectionUtility;
import jakarta.persistence.DiscriminatorValue;
import org.junit.jupiter.api.Test;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.IMAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ImageExtractionDefinitionTest {

    @Test
    void shouldHaveCorrectType() {
        final ImageExtractionDefinition definition = new ImageExtractionDefinition();
        assertEquals(IMAGE, definition.getType());
    }

    @Test
    void shouldHaveCorrectDiscriminatorValue() {
        final DiscriminatorValue discriminatorValue = TestReflectionUtility.assertAnnotationPresentOnClass(
                ImageExtractionDefinition.class,
                DiscriminatorValue.class
        );

        assertEquals("IMAGE", discriminatorValue.value());
    }
}