package com.beluga.api.job.validation.validator;

import com.beluga.api.job.dto.write.WriteScrapingConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.WriteExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.attribute.WriteAttributeExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.description_list.WriteDescriptionListExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.description_list.WriteDescriptionListExtractionDataPointConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.html.WriteHtmlExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.image.WriteImageExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.text.WriteTextExtractionConfigurationDto;
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
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

class ValidFieldNamesValidatorTest {

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

    @Test
    void shouldBeValidWhenFieldNamesDoNotConflict() {
        final WriteScrapingConfigurationDto scrapingConfiguration = Instancio.of(WriteScrapingConfigurationDto.class)
                .set(field(WriteScrapingConfigurationDto::getDataPoints), List.of(
                        Instancio.of(WriteTextExtractionConfigurationDto.class)
                                .set(field(WriteTextExtractionConfigurationDto::getField), "product.price.whole")
                                .create(),
                        Instancio.of(WriteTextExtractionConfigurationDto.class)
                                .set(field(WriteTextExtractionConfigurationDto::getField), "product.price.fractional")
                                .create(),
                        Instancio.of(WriteTextExtractionConfigurationDto.class)
                                .set(field(WriteTextExtractionConfigurationDto::getField), "title")
                                .create(),
                        Instancio.create(WriteImageExtractionConfigurationDto.class)
                ))
                .create();

        assertThat(validator.validate(scrapingConfiguration)).isEmpty();
    }

    @Test
    void shouldRejectDuplicateFieldNames() {
        final WriteScrapingConfigurationDto scrapingConfiguration = Instancio.of(WriteScrapingConfigurationDto.class)
                .set(field(WriteScrapingConfigurationDto::getDataPoints), List.of(
                        Instancio.of(WriteTextExtractionConfigurationDto.class)
                                .set(field(WriteTextExtractionConfigurationDto::getField), "price")
                                .create(),
                        Instancio.of(WriteTextExtractionConfigurationDto.class)
                                .set(field(WriteTextExtractionConfigurationDto::getField), "price")
                                .create()
                ))
                .create();

        final Set<ConstraintViolation<WriteScrapingConfigurationDto>> violations = validator.validate(scrapingConfiguration);

        assertThat(violations)
                .singleElement()
                .extracting(ConstraintViolation::getMessage)
                .isEqualTo("Field name 'price' is used more than once.");
    }

    @Test
    void shouldRejectFieldNameThatIsAlsoPrefixOfAnotherFieldName() {
        final WriteScrapingConfigurationDto scrapingConfiguration = Instancio.of(WriteScrapingConfigurationDto.class)
                .set(field(WriteScrapingConfigurationDto::getDataPoints), List.of(
                        Instancio.of(WriteTextExtractionConfigurationDto.class)
                                .set(field(WriteTextExtractionConfigurationDto::getField), "product.price")
                                .create(),
                        Instancio.of(WriteTextExtractionConfigurationDto.class)
                                .set(field(WriteTextExtractionConfigurationDto::getField), "product")
                                .create()
                ))
                .create();

        final Set<ConstraintViolation<WriteScrapingConfigurationDto>> violations = validator.validate(scrapingConfiguration);

        assertThat(violations)
                .singleElement()
                .extracting(ConstraintViolation::getMessage)
                .isEqualTo("Field name 'product' conflicts with nested field name 'product.price'.");
    }

    @Test
    void shouldRejectConflictingFieldNamesAcrossExtractionTypes() {
        final WriteScrapingConfigurationDto scrapingConfiguration = Instancio.of(WriteScrapingConfigurationDto.class)
                .set(field(WriteScrapingConfigurationDto::getDataPoints), List.of(
                        Instancio.of(WriteTextExtractionConfigurationDto.class)
                                .set(field(WriteTextExtractionConfigurationDto::getField), "product")
                                .create(),
                        Instancio.of(WriteDescriptionListExtractionConfigurationDto.class)
                                .set(field(WriteDescriptionListExtractionConfigurationDto::getDataPoints), List.of(
                                        Instancio.of(WriteDescriptionListExtractionDataPointConfigurationDto.class)
                                                .set(field(WriteDescriptionListExtractionDataPointConfigurationDto::getField), "product.price")
                                                .create()
                                ))
                                .create()
                ))
                .create();

        final Set<ConstraintViolation<WriteScrapingConfigurationDto>> violations = validator.validate(scrapingConfiguration);

        assertThat(violations)
                .singleElement()
                .extracting(ConstraintViolation::getMessage)
                .isEqualTo("Field name 'product' conflicts with nested field name 'product.price'.");
    }

    @ParameterizedTest
    @MethodSource
    void shouldValidateFieldNameOfEachExtractionConfiguration(final WriteExtractionConfigurationDto dataPoint) {
        final WriteScrapingConfigurationDto scrapingConfiguration = Instancio.of(WriteScrapingConfigurationDto.class)
                .set(field(WriteScrapingConfigurationDto::getDataPoints), List.of(
                        dataPoint,
                        Instancio.of(WriteTextExtractionConfigurationDto.class)
                                .set(field(WriteTextExtractionConfigurationDto::getField), "product.price")
                                .create()
                ))
                .create();

        final Set<ConstraintViolation<WriteScrapingConfigurationDto>> violations = validator.validate(scrapingConfiguration);

        assertThat(violations)
                .singleElement()
                .extracting(ConstraintViolation::getMessage)
                .isEqualTo("Field name 'product' conflicts with nested field name 'product.price'.");
    }

    /**
     * One data point per extraction type that has a field name, each using the field name {@code product}. Image
     * extraction has no field name and is covered by {@link #shouldBeValidWhenFieldNamesDoNotConflict()}.
     */
    static Stream<Arguments> shouldValidateFieldNameOfEachExtractionConfiguration() {
        return Stream.of(
                Arguments.of(Instancio.of(WriteAttributeExtractionConfigurationDto.class)
                        .set(field(WriteAttributeExtractionConfigurationDto::getField), "product")
                        .create()),
                Arguments.of(Instancio.of(WriteDescriptionListExtractionConfigurationDto.class)
                        .set(field(WriteDescriptionListExtractionConfigurationDto::getDataPoints), List.of(
                                Instancio.of(WriteDescriptionListExtractionDataPointConfigurationDto.class)
                                        .set(field(WriteDescriptionListExtractionDataPointConfigurationDto::getField), "product")
                                        .create()
                        ))
                        .create()),
                Arguments.of(Instancio.of(WriteHtmlExtractionConfigurationDto.class)
                        .set(field(WriteHtmlExtractionConfigurationDto::getField), "product")
                        .create()),
                Arguments.of(Instancio.of(WriteTextExtractionConfigurationDto.class)
                        .set(field(WriteTextExtractionConfigurationDto::getField), "product")
                        .create())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"product..price", ".product", "product.", "."})
    void shouldRejectMalformedFieldName(final String fieldName) {
        final WriteScrapingConfigurationDto scrapingConfiguration = Instancio.of(WriteScrapingConfigurationDto.class)
                .set(field(WriteScrapingConfigurationDto::getDataPoints), List.of(
                        Instancio.of(WriteTextExtractionConfigurationDto.class)
                                .set(field(WriteTextExtractionConfigurationDto::getField), fieldName)
                                .create()
                ))
                .create();

        final Set<ConstraintViolation<WriteScrapingConfigurationDto>> violations = validator.validate(scrapingConfiguration);

        assertThat(violations)
                .singleElement()
                .extracting(ConstraintViolation::getMessage)
                .isEqualTo("Field name '" + fieldName + "' must not start or end with a dot or contain consecutive dots.");
    }

    @Test
    void shouldReportFieldNameLiterallyWhenItLooksLikeMessageTemplate() {
        final WriteScrapingConfigurationDto scrapingConfiguration = Instancio.of(WriteScrapingConfigurationDto.class)
                .set(field(WriteScrapingConfigurationDto::getDataPoints), List.of(
                        Instancio.of(WriteTextExtractionConfigurationDto.class)
                                .set(field(WriteTextExtractionConfigurationDto::getField), "{jakarta.validation.constraints.NotNull.message}${1+1}")
                                .create(),
                        Instancio.of(WriteTextExtractionConfigurationDto.class)
                                .set(field(WriteTextExtractionConfigurationDto::getField), "{jakarta.validation.constraints.NotNull.message}${1+1}")
                                .create()
                ))
                .create();

        final Set<ConstraintViolation<WriteScrapingConfigurationDto>> violations = validator.validate(scrapingConfiguration);

        assertThat(violations)
                .singleElement()
                .extracting(ConstraintViolation::getMessage)
                .isEqualTo("Field name '{jakarta.validation.constraints.NotNull.message}${1+1}' is used more than once.");
    }
}
