package com.beluga.api.job.dto.write.extraction_configuration;

import com.beluga.api.job.dto.write.extraction_configuration.attribute.WriteAttributeExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.description_list.WriteDescriptionListExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.html.WriteHtmlExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.image.WriteImageExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.text.WriteTextExtractionConfigurationDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class WriteConfigurationDeserializationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MethodSource
    @ParameterizedTest
    void shouldDeserializeToCorrectType(final String fileName,
                                        final Class<?> expectedClass) throws Exception {
        final String fileContent = Files.readString(Path.of("src/test/resources/test-files/api/job/dto/write/extraction_configuration/" + fileName));

        assertInstanceOf(expectedClass, objectMapper.readValue(fileContent, WriteExtractionConfigurationDto.class));
    }

    static Stream<Arguments> shouldDeserializeToCorrectType() {
        return Stream.of(
                Arguments.of("attribute_extraction_configuration.json", WriteAttributeExtractionConfigurationDto.class),
                Arguments.of("description_list_extraction_configuration.json", WriteDescriptionListExtractionConfigurationDto.class),
                Arguments.of("html_extraction_configuration.json", WriteHtmlExtractionConfigurationDto.class),
                Arguments.of("image_extraction_configuration.json", WriteImageExtractionConfigurationDto.class),
                Arguments.of("text_extraction_configuration.json", WriteTextExtractionConfigurationDto.class)
        );
    }
}
