package com.toni.virtualpet.exception;

public class VirtualPetException extends RuntimeException {
    public VirtualPetException(String message) {
        super(message);
    }

    public VirtualPetException(String message, Throwable cause) {
        super(message, cause);
    }
}