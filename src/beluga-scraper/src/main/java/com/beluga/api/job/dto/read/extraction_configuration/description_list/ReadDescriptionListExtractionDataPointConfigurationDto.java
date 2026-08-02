package com.beluga.api.job.dto.read.extraction_configuration.description_list;

import jakarta.validation.constraints.NotBlank;

public class ReadDescriptionListExtractionDataPointConfigurationDto {

    private Long id;

    @NotBlank
    private String dtValue;
    @NotBlank
    private String field;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDtValue() {
        return dtValue;
    }

    public void setDtValue(String dtValue) {
        this.dtValue = dtValue;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
