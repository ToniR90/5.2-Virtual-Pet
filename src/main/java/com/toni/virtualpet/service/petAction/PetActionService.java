package com.toni.virtualpet.service.petAction;

import com.toni.virtualpet.exception.personalizedException.PetActionException;
import com.toni.virtualpet.model.pet.Pet;
import com.toni.virtualpet.model.petAction.PetAction;
import com.toni.virtualpet.model.petAction.enums.ActionType;
import com.toni.virtualpet.model.user.User;
import com.toni.virtualpet.repository.PetActionRepository;
import com.toni.virtualpet.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class PetActionService {

    private final PetActionRepository petActionRepository;
    private final UserRepository userRepository;

    public Pet applyAction(Pet pet, User user, ActionType actionType) {
        switch (actionType) {
            case FEED -> {
                pet.setHunger(Math.min(100, pet.getHunger() + 20));
                pet.setHappiness(Math.min(100, pet.getHappiness() + 10));
            }
            case PLAY -> {
                pet.setHappiness(Math.min(100, pet.getHappiness() + 15));
                pet.setEnergy(Math.max(0, pet.getEnergy() - 10));
                pet.setHunger(Math.max(0, pet.getHunger() - 5));
            }
            case REST -> {
                pet.setEnergy(Math.min(100, pet.getEnergy() + 25));
                pet.setHappiness(Math.min(100, pet.getHappiness() + 5));
                pet.setHunger(Math.max(0, pet.getHunger() - 5));
            }
            case IGNORE -> {
                pet.setHappiness(Math.max(0, pet.getHappiness() - 10));
                pet.setEnergy(Math.max(0, pet.getEnergy() - 5));
                pet.setHunger(Math.max(0, pet.getHunger() - 5));
            }
            case EVOLVE -> {
                if (!pet.canEvolve()) {
                    throw PetActionException.cannotEvolve(pet.getName());
                }
                pet.evolve();
            }
        }

        pet.setLastAction(LocalDateTime.now());
        user.setLastPetAction(LocalDateTime.now());
        pet.addExperience(actionType.getExperienceReward());

        PetAction action = PetAction.builder()
                .actionType(actionType)
                .pet(pet)
                .user(user)
                .experienceGained(actionType.getExperienceReward())
                .build();

        petActionRepository.save(action);
        userRepository.save(user);
        return pet;
    }

}