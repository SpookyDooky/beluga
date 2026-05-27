package com.beluga.properties.datastore;

import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

public class FileSystemProperties {

    @NotBlank
    @NotNull
    private String path = "/data/results";

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }
}
