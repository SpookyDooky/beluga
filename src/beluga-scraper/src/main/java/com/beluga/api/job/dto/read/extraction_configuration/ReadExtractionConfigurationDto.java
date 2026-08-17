package com.beluga.api.job.dto.read.extraction_configuration;

import com.beluga.api.job.dto.read.extraction_configuration.attribute.ReadAttributeExtractionConfigurationDto;
import com.beluga.api.job.dto.read.extraction_configuration.description_list.ReadDescriptionListExtractionConfigurationDto;
import com.beluga.api.job.dto.read.extraction_configuration.html.ReadHtmlExtractionConfigurationDto;
import com.beluga.api.job.dto.read.extraction_configuration.image.ReadImageExtractionConfigurationDto;
import com.beluga.api.job.dto.read.extraction_configuration.text.ReadTextExtractionConfigurationDto;
import com.beluga.model.job_definition.configuration.scraping_configuration.ExtractionType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReadAttributeExtractionConfigurationDto.class, name = "ATTRIBUTE"),
        @JsonSubTypes.Type(value = ReadDescriptionListExtractionConfigurationDto.class, name = "DESCRIPTION_LIST"),
        @JsonSubTypes.Type(value = ReadHtmlExtractionConfigurationDto.class, name = "HTML"),
        @JsonSubTypes.Type(value = ReadImageExtractionConfigurationDto.class, name = "IMAGE"),
        @JsonSubTypes.Type(value = ReadTextExtractionConfigurationDto.class, name = "TEXT"),
})
public abstract class ReadExtractionConfigurationDto {

    private Long id;
    private final ExtractionType type;

    protected ReadExtractionConfigurationDto(final ExtractionType type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public ExtractionType getType() {
        return type;
    }
}
