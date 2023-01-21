package com.timemanager.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entityName, Long id) {
        super(String.format("%s with id=%d not exist!", entityName, id));
    }
}
