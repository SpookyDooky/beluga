package com.beluga.properties.http.user_agent;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public class UserAgentProperties {

    private String value;
    @Valid
    private UserAgentRotationProperties rotation;

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public UserAgentRotationProperties getRotation() {
        return rotation;
    }

    public void setRotation(final UserAgentRotationProperties rotation) {
        this.rotation = rotation;
    }
}
