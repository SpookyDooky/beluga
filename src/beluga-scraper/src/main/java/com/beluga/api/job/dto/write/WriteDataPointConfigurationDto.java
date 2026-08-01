package com.beluga.api.job.dto.write;

import com.beluga.api.job.dto.write.extraction_configuration.WriteExtractionConfigurationDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WriteDataPointConfigurationDto {

    @NotBlank
    private String selector;
    @NotNull
    private WriteExtractionConfigurationDto extraction;

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public WriteExtractionConfigurationDto getExtraction() {
        return extraction;
    }

    public void setExtraction(WriteExtractionConfigurationDto extraction) {
        this.extraction = extraction;
    }
}
