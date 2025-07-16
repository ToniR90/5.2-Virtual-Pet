package com.toni.virtualpel.model;

import java.util.Date;

public class Pet {

    private Long id;
    private String name;
    private Date birthDate;
    private Variant variant;
    private Stage stage;
    private int experience;
    private int level;
    private boolean readyToEvolve;
    private int energy;
    private int happiness;
    private int hunger;
    private User owner;

    public enum Variant {
        MOUNTAIN , LAKE , FOREST
    }

    public enum Stage {
        EGG , YOUNG , ADULT , ANCIENT
    }

    @Override
    public String toString() {
        return "Pet: " + "\n" +
                "Id: " + id + "\n" +
                "Name: " + name + "\n" +
                "Birth Date: " + birthDate + "\n" +
                "Variant: " + variant + "\n" +
                "Stage: " + stage + "\n" +
                "Experience: " + experience + "\n" +
                "Level: " + level + "\n" +
                "Ready To Evolve: " + readyToEvolve + "\n" +
                "Energy: " + energy + "\n" +
                "Happiness: " + happiness + "\n" +
                "Hunger: " + hunger + "\n" +
                "Owner: " + owner + "\n";
    }
}
