package com.beluga.properties.scraping.extraction_configuration.validation.validator;

import com.beluga.properties.scraping.extraction_configuration.ExtractionConfigurationProperties;
import com.beluga.properties.scraping.extraction_configuration.ExtractionTypeProperties;
import com.beluga.properties.scraping.extraction_configuration.validation.annotation.HasExactlyOneExtractionConfiguration;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Map;

/**
 * Validates that exactly one extraction type is configured on {@link ExtractionConfigurationProperties}. The constraints of
 * the configured extraction type itself are validated by its own annotations.
 */
public class HasExactlyOneExtractionConfigurationValidator
        implements ConstraintValidator<HasExactlyOneExtractionConfiguration, ExtractionConfigurationProperties> {

    @Override
    public boolean isValid(final ExtractionConfigurationProperties extractionConfiguration,
                           final ConstraintValidatorContext context) {
        if (extractionConfiguration == null) {
            return true;
        }

        final Map<String, ExtractionTypeProperties> extractionTypes = extractionConfiguration.extractionTypesByName();
        final List<String> configured = extractionTypes.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .map(Map.Entry::getKey)
                .toList();

        if (configured.size() == 1) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                "Exactly one of " + extractionTypes.keySet() + " must be configured, but found: " + configured
        ).addConstraintViolation();
        return false;
    }
}
