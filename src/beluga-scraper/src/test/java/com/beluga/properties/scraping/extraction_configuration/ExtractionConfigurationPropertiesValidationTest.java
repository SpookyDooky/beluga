package com.beluga.properties.scraping.extraction_configuration;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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
                configured(properties -> properties.setAttribute(attribute("a", "link", "href"))),
                configured(properties -> properties.setDescriptionList(descriptionList("dl", List.of(dataPoint("Color", "color"))))),
                configured(properties -> properties.setHtml(html("div", "body"))),
                configured(properties -> properties.setImage(new ImageExtractionConfigurationProperties())),
                configured(properties -> properties.setText(text("p.price", "price")))
        ).map(Arguments::of);
    }

    @Test
    void shouldRejectWhenNoExtractionTypeIsConfigured() {
        final Set<ConstraintViolation<ExtractionConfigurationProperties>> violations =
                validator.validate(new ExtractionConfigurationProperties());

        assertThat(violations)
                .singleElement()
                .extracting(ConstraintViolation::getMessage)
                .isEqualTo("Exactly one of [attribute, description-list, html, image, text] must be configured, but found: []");
    }

    @Test
    void shouldRejectWhenMultipleExtractionTypesAreConfigured() {
        final ExtractionConfigurationProperties properties = new ExtractionConfigurationProperties();
        properties.setHtml(html("div", "body"));
        properties.setText(text("p.price", "price"));

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
                Arguments.of(configured(properties -> properties.setAttribute(attribute(null, "link", "href"))), "attribute.selector"),
                Arguments.of(configured(properties -> properties.setAttribute(attribute("a", " ", "href"))), "attribute.field"),
                Arguments.of(configured(properties -> properties.setAttribute(attribute("a", "link", ""))), "attribute.attribute"),

                // Description list
                Arguments.of(configured(properties -> properties.setDescriptionList(descriptionList(" ", List.of(dataPoint("Color", "color"))))),
                        "descriptionList.selector"),
                Arguments.of(configured(properties -> properties.setDescriptionList(descriptionList("dl", null))),
                        "descriptionList.dataPoints"),
                Arguments.of(configured(properties -> properties.setDescriptionList(descriptionList("dl", List.of()))),
                        "descriptionList.dataPoints"),
                Arguments.of(configured(properties -> properties.setDescriptionList(descriptionList("dl", containingNull))),
                        "descriptionList.dataPoints[0].<list element>"),
                Arguments.of(configured(properties -> properties.setDescriptionList(descriptionList("dl", List.of(dataPoint(null, "color"))))),
                        "descriptionList.dataPoints[0].dtValue"),
                Arguments.of(configured(properties -> properties.setDescriptionList(descriptionList("dl", List.of(dataPoint("Color", ""))))),
                        "descriptionList.dataPoints[0].field"),

                // Html
                Arguments.of(configured(properties -> properties.setHtml(html(null, "body"))), "html.selector"),
                Arguments.of(configured(properties -> properties.setHtml(html("div", " "))), "html.field"),

                // Text
                Arguments.of(configured(properties -> properties.setText(text("", "price"))), "text.selector"),
                Arguments.of(configured(properties -> properties.setText(text("p.price", null))), "text.field")
        );
    }

    private static ExtractionConfigurationProperties configured(final Consumer<ExtractionConfigurationProperties> configurer) {
        final ExtractionConfigurationProperties properties = new ExtractionConfigurationProperties();
        configurer.accept(properties);
        return properties;
    }

    private static AttributeExtractionConfigurationProperties attribute(final String selector, final String field, final String attribute) {
        final AttributeExtractionConfigurationProperties properties = new AttributeExtractionConfigurationProperties();
        properties.setSelector(selector);
        properties.setField(field);
        properties.setAttribute(attribute);
        return properties;
    }

    private static DescriptionListExtractionConfigurationProperties descriptionList(
            final String selector,
            final List<DescriptionListExtractionConfigurationDataPointProperties> dataPoints) {
        final DescriptionListExtractionConfigurationProperties properties = new DescriptionListExtractionConfigurationProperties();
        properties.setSelector(selector);
        properties.setDataPoints(dataPoints);
        return properties;
    }

    private static DescriptionListExtractionConfigurationDataPointProperties dataPoint(final String dtValue, final String field) {
        final DescriptionListExtractionConfigurationDataPointProperties properties = new DescriptionListExtractionConfigurationDataPointProperties();
        properties.setDtValue(dtValue);
        properties.setField(field);
        return properties;
    }

    private static HtmlExtractionConfigurationProperties html(final String selector, final String field) {
        final HtmlExtractionConfigurationProperties properties = new HtmlExtractionConfigurationProperties();
        properties.setSelector(selector);
        properties.setField(field);
        return properties;
    }

    private static TextExtractionConfigurationProperties text(final String selector, final String field) {
        final TextExtractionConfigurationProperties properties = new TextExtractionConfigurationProperties();
        properties.setSelector(selector);
        properties.setField(field);
        return properties;
    }
}
