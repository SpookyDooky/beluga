package com.beluga.api.job.dto.write.extraction_configuration.description_list;

import jakarta.validation.constraints.NotBlank;

public class WriteDescriptionListExtractionDataPointConfigurationDto {

    @NotBlank
    private String dtValue;
    @NotBlank
    private String field;

    public String getDtValue() {
        return dtValue;
    }

    public void setDtValue(final String dtValue) {
        this.dtValue = dtValue;
    }

    public String getField() {
        return field;
    }

    public void setField(final String field) {
        this.field = field;
    }
}
