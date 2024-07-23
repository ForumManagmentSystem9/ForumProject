package com.example.demo.exceptions;

public class EntityDuplicateException extends RuntimeException {
    public EntityDuplicateException(String entity, String fieldName, String fieldValue) {
        super(String.format("%s with %s '%s' already exists", entity, fieldName, fieldValue));
    }

    public EntityDuplicateException(String entity, int id) {
        super(String.format("%s with ID %d already exists", entity, id));
    }
}
