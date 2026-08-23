package com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.attribute;

import com.beluga.test_utils.TestReflectionUtility;
import jakarta.persistence.DiscriminatorValue;
import org.junit.jupiter.api.Test;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.ATTRIBUTE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AttributeExtractionDefinitionTest {

    @Test
    void shouldHaveCorrectType() {
        final AttributeExtractionDefinition definition = new AttributeExtractionDefinition();
        assertEquals(ATTRIBUTE, definition.getType());
    }

    @Test
    void shouldHaveCorrectDiscriminatorValue() {
        final DiscriminatorValue discriminatorValue = TestReflectionUtility.assertAnnotationPresentOnClass(
                AttributeExtractionDefinition.class,
                DiscriminatorValue.class
        );

        assertEquals("ATTRIBUTE", discriminatorValue.value());
    }
}