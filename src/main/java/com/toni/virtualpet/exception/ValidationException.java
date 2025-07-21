package com.toni.virtualpet.exception;

public class ValidationException extends VirtualPetException {
    public ValidationException(String message) {
        super(message);
    }

    public static ValidationException invalidInput(String field, String reason) {
        return new ValidationException("Invalid " + field + ": " + reason);
    }
}
