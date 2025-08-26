package com.toni.virtualpet.exception.personalizedException;

public class InvalidCredentialsException extends VirtualPetException {
    public InvalidCredentialsException() {
        super("Invalid username or password");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
