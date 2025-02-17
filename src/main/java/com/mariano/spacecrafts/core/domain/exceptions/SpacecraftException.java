package com.mariano.spacecrafts.core.domain.exceptions;

import lombok.Getter;

@Getter
public class SpacecraftException extends RuntimeException {

    public SpacecraftException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}