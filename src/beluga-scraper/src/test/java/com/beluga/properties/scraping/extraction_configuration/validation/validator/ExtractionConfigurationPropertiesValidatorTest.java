package com.beluga.properties.scraping.extraction_configuration.validation.validator;

import com.beluga.properties.scraping.extraction_configuration.DescriptionListExtractionConfigurationDataPointProperties;
import com.beluga.properties.scraping.extraction_configuration.ExtractionConfigurationProperties;
import jakarta.validation.ConstraintValidatorContext;
import org.instancio.Instancio;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType.*;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ExtractionConfigurationPropertiesValidatorTest {

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    private final ExtractionConfigurationPropertiesValidator validator = new ExtractionConfigurationPropertiesValidator();

    @ParameterizedTest
    @MethodSource
    void shouldBeValid(final ExtractionConfigurationProperties extractionConfigurationProperties) {
        assertTrue(validator.isValid(extractionConfigurationProperties, constraintValidatorContext));
    }

    static Stream<Arguments> shouldBeValid() {
        return Stream.of(
                Instancio.of(ExtractionConfigurationProperties.class)
                        .set(field(ExtractionConfigurationProperties::getType), ATTRIBUTE)
                        .create(),
                Instancio.of(ExtractionConfigurationProperties.class)
                        .set(field(ExtractionConfigurationProperties::getType), DESCRIPTION_LIST)
                        .create(),
                Instancio.of(ExtractionConfigurationProperties.class)
                        .set(field(ExtractionConfigurationProperties::getType), DESCRIPTION_LIST)
                        .ignore(field(ExtractionConfigurationProperties::getDataPoints))
                        .create(),
                Instancio.of(ExtractionConfigurationProperties.class)
                        .set(field(ExtractionConfigurationProperties::getType), DESCRIPTION_LIST)
                        .set(field(ExtractionConfigurationProperties::getDataPoints), List.of())
                        .create(),
                Instancio.of(ExtractionConfigurationProperties.class)
                        .set(field(ExtractionConfigurationProperties::getType), HTML)
                        .create(),
                Instancio.of(ExtractionConfigurationProperties.class)
                        .set(field(ExtractionConfigurationProperties::getType), IMAGE)
                        .create(),
                Instancio.of(ExtractionConfigurationProperties.class)
                        .set(field(ExtractionConfigurationProperties::getType), TEXT)
                        .create()
        ).map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource
    void shouldBeInvalid(final ExtractionConfigurationProperties extractionConfigurationProperties) {
        assertFalse(validator.isValid(extractionConfigurationProperties, constraintValidatorContext));
    }

    static Stream<Arguments> shouldBeInvalid() {
        return Stream.of(
                        // Attribute
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .set(field(ExtractionConfigurationProperties::getType), ATTRIBUTE)
                                .ignore(field(ExtractionConfigurationProperties::getField))
                                .create(),
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .set(field(ExtractionConfigurationProperties::getType), ATTRIBUTE)
                                .ignore(field(ExtractionConfigurationProperties::getAttribute))
                                .create(),
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .set(field(ExtractionConfigurationProperties::getType), ATTRIBUTE)
                                .ignore(field(ExtractionConfigurationProperties::getSelector))
                                .create(),

                        // Description list
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .set(field(ExtractionConfigurationProperties::getType), DESCRIPTION_LIST)
                                .ignore(field(ExtractionConfigurationProperties::getSelector))
                                .create(),
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .set(field(ExtractionConfigurationProperties::getType), DESCRIPTION_LIST)
                                .set(
                                        field(ExtractionConfigurationProperties::getDataPoints),
                                        List.of(
                                                Instancio.of(DescriptionListExtractionConfigurationDataPointProperties.class)
                                                        .ignore(field(DescriptionListExtractionConfigurationDataPointProperties::getField))
                                                        .create()
                                        )
                                )
                                .create(),
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .set(field(ExtractionConfigurationProperties::getType), DESCRIPTION_LIST)
                                .set(
                                        field(ExtractionConfigurationProperties::getDataPoints),
                                        List.of(
                                                Instancio.of(DescriptionListExtractionConfigurationDataPointProperties.class)
                                                        .ignore(field(DescriptionListExtractionConfigurationDataPointProperties::getDtValue))
                                                        .create()
                                        )
                                )
                                .create(),

                        // Html
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .set(field(ExtractionConfigurationProperties::getType), HTML)
                                .ignore(field(ExtractionConfigurationProperties::getField))
                                .create(),
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .set(field(ExtractionConfigurationProperties::getType), HTML)
                                .ignore(field(ExtractionConfigurationProperties::getSelector))
                                .create(),

                        // Text
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .set(field(ExtractionConfigurationProperties::getType), TEXT)
                                .ignore(field(ExtractionConfigurationProperties::getField))
                                .create(),
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .set(field(ExtractionConfigurationProperties::getType), TEXT)
                                .ignore(field(ExtractionConfigurationProperties::getSelector))
                                .create()
                )
                .map(Arguments::of);
    }
}