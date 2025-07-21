package com.toni.virtualpet.dto.response;

import com.toni.virtualpet.model.Pet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
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

    public static PetResponse from(Pet pet) {
        PetResponse response = new PetResponse();
        response.id = pet.getId();
        response.name = pet.getName();
        response.variant = pet.getVariant().name();
        response.variantDisplayName = pet.getVariant().getDisplayName();
        response.variantDescription = pet.getVariant().getDescription();
        response.variantColorName = pet.getVariant().getColorName();
        response.stage = pet.getStage().name();
        response.stageDisplayName = pet.getStage().getDisplayName();
        response.experience = pet.getExperience();
        response.energy = pet.getEnergy();
        response.happiness = pet.getHappiness();
        response.hunger = pet.getHunger();
        response.createdAt = pet.getCreatedAt();
        response.lastAction = pet.getLastAction();
        response.canEvolve = pet.canEvolve();
        response.ownerUsername = pet.getOwner().getUsername();
        response.sprite = pet.getSprite();
        return response;
    }
}