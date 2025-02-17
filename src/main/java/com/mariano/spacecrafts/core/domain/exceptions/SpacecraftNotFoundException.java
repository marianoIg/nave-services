package com.mariano.spacecrafts.core.domain.exceptions;

import lombok.Getter;

@Getter
public class SpacecraftNotFoundException extends SpacecraftException {
    private final SpacecraftsError errorType;
    private final String value;

    public SpacecraftNotFoundException(SpacecraftsError errorType, String value) {
        super(String.format(errorType.getDescription(), value));
        this.errorType = errorType;
        this.value = value;
    }
}
