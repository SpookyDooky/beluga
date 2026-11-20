package com.beluga.api.job.dto.write;

import com.beluga.api.job.dto.base.BaseExecutionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.text.WriteTextExtractionConfigurationDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

class WriteJobDefinitionDtoValidationTest {

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
    void shouldValidateExecutionConfiguration() {
        final WriteJobDefinitionDto jobDefinition = Instancio.of(WriteJobDefinitionDto.class)
                .set(field(BaseExecutionConfigurationDto::getTasksPerSecond), -1.0)
                .create();

        final Set<ConstraintViolation<WriteJobDefinitionDto>> violations = validator.validate(jobDefinition);

        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactly("execution.tasksPerSecond");
    }

    @Test
    void shouldValidateScrapingConfiguration() {
        final WriteJobDefinitionDto jobDefinition = Instancio.of(WriteJobDefinitionDto.class)
                .set(field(WriteScrapingConfigurationDto::getDataPoints), List.of(
                        Instancio.of(WriteTextExtractionConfigurationDto.class)
                                .set(field(WriteTextExtractionConfigurationDto::getField), "price")
                                .create(),
                        Instancio.of(WriteTextExtractionConfigurationDto.class)
                                .set(field(WriteTextExtractionConfigurationDto::getField), "price")
                                .create()
                ))
                .create();

        final Set<ConstraintViolation<WriteJobDefinitionDto>> violations = validator.validate(jobDefinition);

        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactly("scraping");
    }
}
