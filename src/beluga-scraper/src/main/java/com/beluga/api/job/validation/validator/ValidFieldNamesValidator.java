package com.beluga.api.job.validation.validator;

import com.beluga.api.job.dto.write.WriteScrapingConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.WriteExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.attribute.WriteAttributeExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.description_list.WriteDescriptionListExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.description_list.WriteDescriptionListExtractionDataPointConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.image.WriteImageExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.text.WriteTextExtractionConfigurationDto;
import com.beluga.api.job.validation.annotation.ValidFieldNames;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.*;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

/**
 * Validates whether field names for data storage do not class or overlap.
 */
public class ValidFieldNamesValidator implements ConstraintValidator<ValidFieldNames, WriteScrapingConfigurationDto> {

    @Override
    public boolean isValid(final WriteScrapingConfigurationDto scrapingConfiguration,
                           final ConstraintValidatorContext context) {
        if (scrapingConfiguration == null || scrapingConfiguration.getDataPoints() == null) {
            return true;
        }

        final List<String> fieldNames = scrapingConfiguration.getDataPoints().stream()
                .filter(Objects::nonNull)
                .flatMap(this::getFieldNames)
                .filter(fieldName -> fieldName != null && !fieldName.isBlank())
                .toList();

        final List<String> errors = new ArrayList<>();
        errors.addAll(findMalformedFieldNames(fieldNames));
        errors.addAll(findDuplicateFieldNames(fieldNames));
        errors.addAll(findConflictingFieldNames(fieldNames));

        if (errors.isEmpty()) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        errors.forEach(error -> context.buildConstraintViolationWithTemplate(escape(error)).addConstraintViolation());
        return false;
    }

    private Stream<String> getFieldNames(final WriteExtractionConfigurationDto dataPoint) {
        return switch (dataPoint) {
            case WriteAttributeExtractionConfigurationDto attribute -> Stream.of(attribute.getField());
            case WriteDescriptionListExtractionConfigurationDto descriptionList -> getFieldNames(descriptionList);
            case WriteImageExtractionConfigurationDto ignored -> Stream.empty();
            case WriteTextExtractionConfigurationDto text -> Stream.of(text.getField());
            default -> throw new IllegalArgumentException("Unsupported extraction configuration");
        };
    }

    private Stream<String> getFieldNames(final WriteDescriptionListExtractionConfigurationDto dto) {
        return dto.getDataPoints()
                .stream()
                .filter(Objects::nonNull)
                .map(WriteDescriptionListExtractionDataPointConfigurationDto::getField);
    }

    private List<String> findMalformedFieldNames(final List<String> fieldNames) {
        return fieldNames.stream()
                .filter(this::isFieldNameMalformed)
                .map(fieldName -> "Field name '" + fieldName + "' must not start or end with a dot or contain consecutive dots.")
                .toList();
    }

    private boolean isFieldNameMalformed(final String fieldName) {
        // -1 prevents empty string discarding
        return Arrays.asList(fieldName.split("\\.", -1)).contains("");
    }

    private List<String> findDuplicateFieldNames(final List<String> fieldNames) {
        final Set<String> distinctFieldNames = new HashSet<>();

        return fieldNames.stream()
                .filter(not(distinctFieldNames::add))
                .distinct()
                .map(fieldName -> "Field name '" + fieldName + "' is used more than once.")
                .toList();
    }

    private List<String> findConflictingFieldNames(final List<String> fieldNames) {
        final Set<String> distinctFieldNames = new LinkedHashSet<>(fieldNames);
        final List<String> errors = new ArrayList<>();

        for (final String fieldName : distinctFieldNames) {
            for (final String otherFieldName : distinctFieldNames) {
                if (otherFieldName.startsWith(fieldName + ".")) {
                    errors.add("Field name '" + fieldName + "' conflicts with nested field name '" + otherFieldName + "'.");
                }
            }
        }

        return errors;
    }

    /**
     * Field names are user input and the violation message is interpreted as a message template, so the template
     * characters are escaped to report the field name literally.
     */
    private String escape(final String message) {
        return message.replaceAll("([\\\\{}$])", "\\\\$1");
    }
}
