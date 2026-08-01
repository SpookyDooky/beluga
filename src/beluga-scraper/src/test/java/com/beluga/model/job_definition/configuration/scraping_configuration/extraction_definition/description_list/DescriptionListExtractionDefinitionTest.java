package com.beluga.model.job_definition.configuration.scraping_configuration.extraction_definition.description_list;

import com.beluga.test_utils.TestReflectionUtility;
import jakarta.persistence.DiscriminatorValue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.DESCRIPTION_LIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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

    @Test
    void shouldSetDataPoints() {
        final DescriptionListExtractionDataPointDefinition dataPointDefinition = mock();

        final DescriptionListExtractionDefinition definition = new DescriptionListExtractionDefinition();
        definition.setDataPoints(List.of(dataPointDefinition));

        verify(dataPointDefinition).setDescriptionListExtractionDefinition(definition);
    }
}