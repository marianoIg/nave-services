package com.mariano.spacecrafts.core.domain.exceptions;

import lombok.Getter;

@Getter
public class SpacecraftAlreadyExistsException extends SpacecraftException {

    private final SpacecraftsError errorType;
    private final String value;

    public SpacecraftAlreadyExistsException(SpacecraftsError errorType, String value) {
        super(value);
        this.errorType = errorType;
        this.value = value;
    }
}
