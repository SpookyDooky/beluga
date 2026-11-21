package com.beluga.properties.scraping.extraction_configuration;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.instancio.settings.Keys.COLLECTION_MAX_SIZE;

/**
 * Validates {@link ExtractionConfigurationProperties} with a real Bean Validation provider, covering both the exactly-one rule
 * and the constraints of each extraction type.
 */
class ExtractionConfigurationPropertiesValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    @ParameterizedTest
    @MethodSource
    void shouldBeValid(final ExtractionConfigurationProperties properties) {
        assertThat(validator.validate(properties)).isEmpty();
    }

    static Stream<Arguments> shouldBeValid() {
        return Stream.of(
                Instancio.of(ExtractionConfigurationProperties.class)
                        .ignore(field(ExtractionConfigurationProperties::getDescriptionList))
                        .ignore(field(ExtractionConfigurationProperties::getHtml))
                        .ignore(field(ExtractionConfigurationProperties::getImage))
                        .ignore(field(ExtractionConfigurationProperties::getText))
                        .create(),
                Instancio.of(ExtractionConfigurationProperties.class)
                        .ignore(field(ExtractionConfigurationProperties::getAttribute))
                        .ignore(field(ExtractionConfigurationProperties::getHtml))
                        .ignore(field(ExtractionConfigurationProperties::getImage))
                        .ignore(field(ExtractionConfigurationProperties::getText))
                        .create(),
                Instancio.of(ExtractionConfigurationProperties.class)
                        .ignore(field(ExtractionConfigurationProperties::getAttribute))
                        .ignore(field(ExtractionConfigurationProperties::getDescriptionList))
                        .ignore(field(ExtractionConfigurationProperties::getImage))
                        .ignore(field(ExtractionConfigurationProperties::getText))
                        .create(),
                Instancio.of(ExtractionConfigurationProperties.class)
                        .ignore(field(ExtractionConfigurationProperties::getAttribute))
                        .ignore(field(ExtractionConfigurationProperties::getDescriptionList))
                        .ignore(field(ExtractionConfigurationProperties::getHtml))
                        .ignore(field(ExtractionConfigurationProperties::getText))
                        .create(),
                Instancio.of(ExtractionConfigurationProperties.class)
                        .ignore(field(ExtractionConfigurationProperties::getAttribute))
                        .ignore(field(ExtractionConfigurationProperties::getDescriptionList))
                        .ignore(field(ExtractionConfigurationProperties::getHtml))
                        .ignore(field(ExtractionConfigurationProperties::getImage))
                        .create()
        ).map(Arguments::of);
    }

    @Test
    void shouldRejectWhenNoExtractionTypeIsConfigured() {
        final ExtractionConfigurationProperties properties =
                Instancio.of(ExtractionConfigurationProperties.class)
                        .ignore(field(ExtractionConfigurationProperties::getAttribute))
                        .ignore(field(ExtractionConfigurationProperties::getDescriptionList))
                        .ignore(field(ExtractionConfigurationProperties::getHtml))
                        .ignore(field(ExtractionConfigurationProperties::getImage))
                        .ignore(field(ExtractionConfigurationProperties::getText))
                        .create();

        final Set<ConstraintViolation<ExtractionConfigurationProperties>> violations = validator.validate(properties);

        assertThat(violations)
                .singleElement()
                .extracting(ConstraintViolation::getMessage)
                .isEqualTo("Exactly one of [attribute, description-list, html, image, text] must be configured, but found: []");
    }

    @Test
    void shouldRejectWhenMultipleExtractionTypesAreConfigured() {
        final ExtractionConfigurationProperties properties =
                Instancio.of(ExtractionConfigurationProperties.class)
                        .ignore(field(ExtractionConfigurationProperties::getAttribute))
                        .ignore(field(ExtractionConfigurationProperties::getDescriptionList))
                        .ignore(field(ExtractionConfigurationProperties::getImage))
                        .create();

        final Set<ConstraintViolation<ExtractionConfigurationProperties>> violations = validator.validate(properties);

        assertThat(violations)
                .singleElement()
                .extracting(ConstraintViolation::getMessage)
                .isEqualTo("Exactly one of [attribute, description-list, html, image, text] must be configured, but found: [html, text]");
    }

    @ParameterizedTest
    @MethodSource
    void shouldRejectInvalidExtractionType(final ExtractionConfigurationProperties properties, final String expectedPropertyPath) {
        final Set<ConstraintViolation<ExtractionConfigurationProperties>> violations = validator.validate(properties);

        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactly(expectedPropertyPath);
    }

    static Stream<Arguments> shouldRejectInvalidExtractionType() {
        final List<DescriptionListExtractionConfigurationDataPointProperties> containingNull = new ArrayList<>();
        containingNull.add(null);

        return Stream.of(
                // Attribute
                Arguments.of(
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .ignore(field(ExtractionConfigurationProperties::getDescriptionList))
                                .ignore(field(ExtractionConfigurationProperties::getHtml))
                                .ignore(field(ExtractionConfigurationProperties::getImage))
                                .ignore(field(ExtractionConfigurationProperties::getText))
                                .set(field(AttributeExtractionConfigurationProperties::getSelector), null)
                                .create(),
                        "attribute.selector"
                ),
                Arguments.of(
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .ignore(field(ExtractionConfigurationProperties::getDescriptionList))
                                .ignore(field(ExtractionConfigurationProperties::getHtml))
                                .ignore(field(ExtractionConfigurationProperties::getImage))
                                .ignore(field(ExtractionConfigurationProperties::getText))
                                .set(field(AttributeExtractionConfigurationProperties::getField), " ")
                                .create(),
                        "attribute.field"
                ),
                Arguments.of(
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .ignore(field(ExtractionConfigurationProperties::getDescriptionList))
                                .ignore(field(ExtractionConfigurationProperties::getHtml))
                                .ignore(field(ExtractionConfigurationProperties::getImage))
                                .ignore(field(ExtractionConfigurationProperties::getText))
                                .set(field(AttributeExtractionConfigurationProperties::getAttribute), "")
                                .create(),
                        "attribute.attribute"
                ),

                // Description list
                Arguments.of(
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .ignore(field(ExtractionConfigurationProperties::getAttribute))
                                .ignore(field(ExtractionConfigurationProperties::getHtml))
                                .ignore(field(ExtractionConfigurationProperties::getImage))
                                .ignore(field(ExtractionConfigurationProperties::getText))
                                .set(field(DescriptionListExtractionConfigurationProperties::getSelector), " ")
                                .create(),
                        "descriptionList.selector"
                ),
                Arguments.of(
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .ignore(field(ExtractionConfigurationProperties::getAttribute))
                                .ignore(field(ExtractionConfigurationProperties::getHtml))
                                .ignore(field(ExtractionConfigurationProperties::getImage))
                                .ignore(field(ExtractionConfigurationProperties::getText))
                                .set(field(DescriptionListExtractionConfigurationProperties::getDataPoints), null)
                                .create(),
                        "descriptionList.dataPoints"
                ),
                Arguments.of(
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .ignore(field(ExtractionConfigurationProperties::getAttribute))
                                .ignore(field(ExtractionConfigurationProperties::getHtml))
                                .ignore(field(ExtractionConfigurationProperties::getImage))
                                .ignore(field(ExtractionConfigurationProperties::getText))
                                .set(field(DescriptionListExtractionConfigurationProperties::getDataPoints), List.of())
                                .create(),
                        "descriptionList.dataPoints"
                ),
                Arguments.of(
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .ignore(field(ExtractionConfigurationProperties::getAttribute))
                                .ignore(field(ExtractionConfigurationProperties::getHtml))
                                .ignore(field(ExtractionConfigurationProperties::getImage))
                                .ignore(field(ExtractionConfigurationProperties::getText))
                                .set(field(DescriptionListExtractionConfigurationProperties::getDataPoints), containingNull)
                                .create(),
                        "descriptionList.dataPoints[0].<list element>"
                ),
                Arguments.of(
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .ignore(field(ExtractionConfigurationProperties::getAttribute))
                                .ignore(field(ExtractionConfigurationProperties::getHtml))
                                .ignore(field(ExtractionConfigurationProperties::getImage))
                                .ignore(field(ExtractionConfigurationProperties::getText))
                                .withSetting(COLLECTION_MAX_SIZE, 1)
                                .set(field(DescriptionListExtractionConfigurationDataPointProperties::getDtValue), null)
                                .create(),
                        "descriptionList.dataPoints[0].dtValue"
                ),
                Arguments.of(
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .ignore(field(ExtractionConfigurationProperties::getAttribute))
                                .ignore(field(ExtractionConfigurationProperties::getHtml))
                                .ignore(field(ExtractionConfigurationProperties::getImage))
                                .ignore(field(ExtractionConfigurationProperties::getText))
                                .withSetting(COLLECTION_MAX_SIZE, 1)
                                .set(field(DescriptionListExtractionConfigurationDataPointProperties::getField), "")
                                .create(),
                        "descriptionList.dataPoints[0].field"
                ),

                // Html
                Arguments.of(
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .ignore(field(ExtractionConfigurationProperties::getAttribute))
                                .ignore(field(ExtractionConfigurationProperties::getDescriptionList))
                                .ignore(field(ExtractionConfigurationProperties::getImage))
                                .ignore(field(ExtractionConfigurationProperties::getText))
                                .set(field(HtmlExtractionConfigurationProperties::getSelector), null)
                                .create(),
                        "html.selector"
                ),
                Arguments.of(
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .ignore(field(ExtractionConfigurationProperties::getAttribute))
                                .ignore(field(ExtractionConfigurationProperties::getDescriptionList))
                                .ignore(field(ExtractionConfigurationProperties::getImage))
                                .ignore(field(ExtractionConfigurationProperties::getText))
                                .set(field(HtmlExtractionConfigurationProperties::getField), " ")
                                .create(),
                        "html.field"
                ),

                // Text
                Arguments.of(
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .ignore(field(ExtractionConfigurationProperties::getAttribute))
                                .ignore(field(ExtractionConfigurationProperties::getDescriptionList))
                                .ignore(field(ExtractionConfigurationProperties::getHtml))
                                .ignore(field(ExtractionConfigurationProperties::getImage))
                                .set(field(TextExtractionConfigurationProperties::getSelector), "")
                                .create(),
                        "text.selector"
                ),
                Arguments.of(
                        Instancio.of(ExtractionConfigurationProperties.class)
                                .ignore(field(ExtractionConfigurationProperties::getAttribute))
                                .ignore(field(ExtractionConfigurationProperties::getDescriptionList))
                                .ignore(field(ExtractionConfigurationProperties::getHtml))
                                .ignore(field(ExtractionConfigurationProperties::getImage))
                                .set(field(TextExtractionConfigurationProperties::getField), null)
                                .create(),
                        "text.field"
                )
        );
    }
}
