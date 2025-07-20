package com.toni.virtualpel.exception;

public class PetNotFoundException extends VirtualPetException {
    public PetNotFoundException(String message) {
        super(message);
    }

    public PetNotFoundException(Long petId) {
        super("Pet not found with id: " + petId);
    }
}
