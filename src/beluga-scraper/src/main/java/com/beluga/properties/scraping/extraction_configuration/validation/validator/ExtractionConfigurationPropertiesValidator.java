package com.beluga.properties.scraping.extraction_configuration.validation.validator;

import com.beluga.properties.scraping.extraction_configuration.DescriptionListExtractionConfigurationDataPointProperties;
import com.beluga.properties.scraping.extraction_configuration.ExtractionConfigurationProperties;
import com.beluga.properties.scraping.extraction_configuration.validation.annotation.ExtractionConfiguration;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.function.Supplier;

/**
 * Validates if the configured {@link ExtractionConfigurationProperties} are valid.
 */
public class ExtractionConfigurationPropertiesValidator
        implements ConstraintValidator<ExtractionConfiguration, ExtractionConfigurationProperties> {

    @Override
    public boolean isValid(final ExtractionConfigurationProperties extractionConfiguration,
                           final ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        return switch(extractionConfiguration.getType()) {
            case ATTRIBUTE -> isValidAttributeExtractionConfiguration(extractionConfiguration, context);
            case DESCRIPTION_LIST -> isValidDescriptionListExtractionConfiguration(extractionConfiguration, context);
            case HTML -> isValidHtmlExtractionConfiguration(extractionConfiguration, context);
            case IMAGE -> isValidImageExtractionConfiguration(extractionConfiguration, context);
            case TEXT -> isValidTextExtractionConfiguration(extractionConfiguration, context);
        };
    }

    /**
     * Validates if an extraction configuration is a valid attribute extraction configuration.
     */
    private boolean isValidAttributeExtractionConfiguration(final ExtractionConfigurationProperties extractionConfiguration,
                                                            final ConstraintValidatorContext context) {
        return isNotNullOrBlank("selector", extractionConfiguration::getSelector, context) &&
                isNotNullOrBlank("field", extractionConfiguration::getField, context) &&
                isNotNullOrBlank("attribute", extractionConfiguration::getAttribute, context);
    }

    private boolean isNotNullOrBlank(final String propertyName,
                                     final Supplier<String> valueSupplier,
                                     final ConstraintValidatorContext context) {
        final String value = valueSupplier.get();

        if (value == null || value.isBlank()) {
            context.buildConstraintViolationWithTemplate(propertyName + "is required and can not be null or blank.");
            return false;
        }

         return true;
    }

    private boolean isValidDescriptionListExtractionConfiguration(final ExtractionConfigurationProperties extractionConfiguration,
                                                                  final ConstraintValidatorContext context) {
        return isNotNullOrBlank("selector", extractionConfiguration::getSelector, context) &&
                isNotNullOrBlank("field", extractionConfiguration::getField, context) &&
                isValidDataPointsConfiguration(extractionConfiguration.getDataPoints(), context);
    }

    private boolean isValidDataPointsConfiguration(final List<DescriptionListExtractionConfigurationDataPointProperties> dataPoints,
                                                   final ConstraintValidatorContext context) {
        if (dataPoints == null || dataPoints.isEmpty()) {
            return true;
        }

        return dataPoints.stream()
                .allMatch(dataPoint -> isValidDataPointConfiguration(dataPoint, context));
    }

    private boolean isValidDataPointConfiguration(final DescriptionListExtractionConfigurationDataPointProperties dataPoint,
                                                  final ConstraintValidatorContext context) {
        return isNotNullOrBlank("dtValue", dataPoint::getDtValue, context) &&
                isNotNullOrBlank("field", dataPoint::getField, context);
    }

    private boolean isValidHtmlExtractionConfiguration(final ExtractionConfigurationProperties extractionConfiguration,
                                                       final ConstraintValidatorContext context) {
        return isNotNullOrBlank("selector", extractionConfiguration::getSelector, context) &&
                isNotNullOrBlank("field", extractionConfiguration::getField, context);
    }

    private boolean isValidImageExtractionConfiguration(final ExtractionConfigurationProperties extractionConfiguration,
                                                        final ConstraintValidatorContext context) {
        return true;
    }

    private boolean isValidTextExtractionConfiguration(final ExtractionConfigurationProperties extractionConfiguration,
                                                       final ConstraintValidatorContext context) {
        return isNotNullOrBlank("selector", extractionConfiguration::getSelector, context) &&
                isNotNullOrBlank("field", extractionConfiguration::getField, context);
    }
}
