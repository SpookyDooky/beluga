package com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.description_list;

import com.beluga.test_utils.TestReflectionUtility;
import jakarta.persistence.DiscriminatorValue;
import org.junit.jupiter.api.Test;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.DESCRIPTION_LIST;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DescriptionListExtractionDefinitionTest {

    @Test
    void shouldHaveCorrectType() {
        final DescriptionListExtractionDefinition definition = new DescriptionListExtractionDefinition();
        assertEquals(DESCRIPTION_LIST, definition.getType());
    }

    @Test
    void shouldHaveCorrectDiscriminatorValue() {
        final DiscriminatorValue discriminatorValue = TestReflectionUtility.assertAnnotationPresentOnClass(
                DescriptionListExtractionDefinition.class,
                DiscriminatorValue.class
        );

        assertEquals("DESCRIPTION_LIST", discriminatorValue.value());
    }
}