package com.toni.virtualpet.exception;

public class PetNotOwnedException extends VirtualPetException {
    public PetNotOwnedException(String message) {
        super(message);
    }

    public PetNotOwnedException(Long petId, Long userId) {
        super("Pet " + petId + " is not owned by user " + userId);
    }
}
