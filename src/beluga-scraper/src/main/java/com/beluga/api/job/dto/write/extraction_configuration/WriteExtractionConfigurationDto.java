package com.beluga.api.job.dto.write.extraction_configuration;

import com.beluga.api.job.dto.write.extraction_configuration.attribute.WriteAttributeExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.description_list.WriteDescriptionListExtractionDataPointConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.html.WriteHtmlExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.image.WriteImageExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.text.WriteTextExtractionConfigurationDto;
import com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = WriteAttributeExtractionConfigurationDto.class, name = "ATTRIBUTE"),
        @JsonSubTypes.Type(value = WriteDescriptionListExtractionDataPointConfigurationDto.class, name = "DESCRIPTION_LIST"),
        @JsonSubTypes.Type(value = WriteHtmlExtractionConfigurationDto.class, name = "HTML"),
        @JsonSubTypes.Type(value = WriteImageExtractionConfigurationDto.class, name = "IMAGE"),
        @JsonSubTypes.Type(value = WriteTextExtractionConfigurationDto.class, name = "TEXT"),
})
public abstract class WriteExtractionConfigurationDto {

    private final ExtractionType type;

    protected WriteExtractionConfigurationDto(final ExtractionType type) {
        this.type = type;
    }

    public ExtractionType getType() {
        return type;
    }
}
