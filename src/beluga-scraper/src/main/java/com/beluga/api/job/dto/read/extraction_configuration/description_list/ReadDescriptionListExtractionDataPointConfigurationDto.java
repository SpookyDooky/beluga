package com.beluga.api.job.dto.read.extraction_configuration.description_list;

import jakarta.validation.constraints.NotBlank;

public class ReadDescriptionListExtractionDataPointConfigurationDto {

    @NotBlank
    private String dtValue;
    @NotBlank
    private String field;

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
