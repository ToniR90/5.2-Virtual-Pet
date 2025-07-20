package com.toni.virtualpel.exception;

public class UnauthorizedException extends VirtualPetException {
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException() {
        super("Access denied");
    }
}
