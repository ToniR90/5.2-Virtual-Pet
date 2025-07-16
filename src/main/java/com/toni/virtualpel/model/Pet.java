package com.toni.virtualpel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pet_name")
    @NotBlank
    private String name;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private Variant variant;

    @Enumerated(EnumType.STRING)
    private Stage stage = Stage.EGG;

    private int experience = 0;
    private boolean readyToEvolve;
    private int energy = 10;
    private int happiness = 10;
    private int hunger = 10;
    private User owner;

    public enum Variant {
        MOUNTAIN , SWAMP , FOREST
    }

    public enum Stage {
        EGG , YOUNG , ADULT , ANCIENT
    }

    @Override
    public String toString() {
        return "Pet: " + "\n" +
                "Id: " + id + "\n" +
                "Name: " + name + "\n" +
                "Variant: " + variant + "\n" +
                "Stage: " + stage + "\n" +
                "Experience: " + experience + "\n" +
                "Ready To Evolve: " + readyToEvolve + "\n" +
                "Energy: " + energy + "\n" +
                "Happiness: " + happiness + "\n" +
                "Hunger: " + hunger + "\n" +
                "Owner: " + owner + "\n";
    }
}
