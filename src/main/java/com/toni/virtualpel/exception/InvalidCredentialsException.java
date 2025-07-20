package com.toni.virtualpel.exception;

public class InvalidCredentialsException extends VirtualPetException {
    public InvalidCredentialsException() {
        super("Invalid username or password");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
