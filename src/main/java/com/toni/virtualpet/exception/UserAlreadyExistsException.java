package com.toni.virtualpet.exception;

public class UserAlreadyExistsException extends VirtualPetException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String field, String value) {
        super("User already exists with " + field + ": " + value);
    }
}
