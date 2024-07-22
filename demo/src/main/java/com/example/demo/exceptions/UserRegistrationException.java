package com.example.demo.exceptions;

public class UserRegistrationException extends RuntimeException{
    public UserRegistrationException(String type, int id) {
        this(type, "id", String.valueOf(id));
    }

    public UserRegistrationException(String type, String attribute, String value) {
        super(String.format("%s with %s %s already exist", type, attribute, value));
    }
}
