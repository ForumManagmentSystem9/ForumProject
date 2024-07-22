package com.example.demo.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entity, int id) {
        super(String.format("%s with ID %d not found", entity, id));
    }

    public EntityNotFoundException(String entity, String fieldName, String fieldValue) {
        super(String.format("%s with %s '%s' not found", entity, fieldName, fieldValue));
    }
}
