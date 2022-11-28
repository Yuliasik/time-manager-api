package com.timemanager.exception;

public class EntityValidationFailedException extends RuntimeException {

    public EntityValidationFailedException(String message) {
        super(message);
    }
}
