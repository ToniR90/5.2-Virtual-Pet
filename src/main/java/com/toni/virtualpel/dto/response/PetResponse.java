package com.toni.virtualpel.dto.response;

import com.toni.virtualpel.model.Pet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class PetResponse {
    private Long id;
    private String name;
    private String variant;
    private String variantDisplayName;
    private String variantDescription;
    private String variantColorName;
    private String stage;
    private String stageDisplayName;
    private Integer experience;
    private Integer energy;
    private Integer happiness;
    private Integer hunger;
    private LocalDateTime createdAt;
    private LocalDateTime lastAction;
    private boolean canEvolve;
    private String ownerUsername;
    private String sprite;

    public PetResponse(Pet pet) {
        this.id = pet.getId();
        this.name = pet.getName();
        this.variant = pet.getVariant().name();
        this.variantDisplayName = pet.getVariant().getDisplayName();
        this.variantDescription = pet.getVariant().getDescription();
        this.variantColorName = pet.getVariant().getColorName();
        this.stage = pet.getStage().name();
        this.stageDisplayName = pet.getStage().getDisplayName();
        this.experience = pet.getExperience();
        this.energy = pet.getEnergy();
        this.happiness = pet.getHappiness();
        this.hunger = pet.getHunger();
        this.createdAt = pet.getCreatedAt();
        this.lastAction = pet.getLastAction();
        this.canEvolve = pet.canEvolve();
        this.ownerUsername = pet.getOwner().getUserName();
        this.sprite = pet.getSprite();
    }
}
