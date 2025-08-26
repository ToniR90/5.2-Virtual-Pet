package com.toni.virtualpet.exception.personalizedException;

public class PetActionException extends VirtualPetException {
    public PetActionException(String message) {
        super(message);
    }

    public static PetActionException cannotEvolve(String petName) {
        return new PetActionException("Pet " + petName + " cannot evolve yet");
    }

    public static PetActionException insufficientStats(String action, String reason) {
        return new PetActionException("Cannot perform " + action + ": " + reason);
    }
}
