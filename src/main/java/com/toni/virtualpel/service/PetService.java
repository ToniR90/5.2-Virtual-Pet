package com.toni.virtualpel.service;

import com.toni.virtualpel.dto.CreatePetRequest;
import com.toni.virtualpel.dto.PetResponse;
import com.toni.virtualpel.model.Pet;
import com.toni.virtualpel.model.PetAction;
import com.toni.virtualpel.model.User;
import com.toni.virtualpel.model.enums.ActionType;
import com.toni.virtualpel.model.enums.Stage;
import com.toni.virtualpel.repository.PetActionRepository;
import com.toni.virtualpel.repository.PetRepository;
import com.toni.virtualpel.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Transactional
public class PetService {

    private static final Logger logger = LoggerFactory.getLogger(PetService.class);

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetActionRepository petActionRepository;

    public PetResponse createPet(CreatePetRequest request) {
        User currentUser = getCurrentUser();
        logger.info("Creating Pet '{}' variant {} for user: {}" , request.getName() , request.getVariant() , currentUser.getUserName());

        Pet pet = new Pet(request.getName(), request.getVariant(), currentUser);
        Pet savedPet = petRepository.save(pet);

        logger.info("Pet created: {}", savedPet);
        return new PetResponse(savedPet);
    }

    public List<PetResponse> getUserPets() {
        User currentUser = getCurrentUser();
        logger.info("Getting pets from user: {}" , currentUser.getUserName());

        List<Pet> pets = petRepository.findByOwner(currentUser);
        return pets.stream()
                .map(PetResponse::new)
                .collect(Collectors.toList());
    }

    public PetResponse getPetById(Long petId) {
        User currentUser = getCurrentUser();
        Pet pet = petRepository.findByIdAndOwner(petId, currentUser)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        return new PetResponse(pet);
    }

    public PetResponse feedPet(Long petId) {
        return performAction(petId, ActionType.FEED, "feed");
    }

    public PetResponse playWithPet(Long petId) {
        return performAction(petId, ActionType.PLAY, "play");
    }

    public PetResponse restPet(Long petId) {
        return performAction(petId, ActionType.REST, "rest");
    }

    public PetResponse ignorePet(Long petId) {
        return performAction(petId, ActionType.IGNORE, "ignore");
    }

    private PetResponse performAction(Long petId , ActionType actionType , String actionName) {
        User currentUser = getCurrentUser();
        Pet pet = petRepository.findByIdAndOwner(petId, currentUser)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        logger.info("Action in progress '{}' on pet '{}' from user: {}",
                actionName, pet.getName(), currentUser.getUserName());

        switch(actionType) {
            case FEED -> pet.feed();
            case PLAY -> pet.play();
            case REST -> pet.rest();
            case IGNORE -> pet.ignore();
        }

        /*Pet savedPet = petRepository.save(pet);
        PetAction action = new PetAction(actionType, savedPet, currentUser);
        petActionRepository.save(action);

        logger.info("Action '{}' done. Pet updated: {}", actionName, savedPet);
        return new PetResponse(savedPet);*/
    }

    public PetResponse evolvePet(Long petId) {
        User currentUser = getCurrentUser();
        Pet pet = petRepository.findByIdAndOwner(petId, currentUser)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        if (!pet.canEvolve()) {
            throw new RuntimeException("Pet can't evolve yet");
        }

        logger.info("Evolving Pet '{}' from user: {}", pet.getName(), currentUser.getUserName());

        Stage oldStage = pet.getStage();
        pet.evolve();

        Pet savedPet = petRepository.save(pet);

        /*PetAction action = new PetAction(ActionType.EVOLVE, savedPet, currentUser);
        petActionRepository.save(action);

        logger.info("Pet evolved from {} to {}", oldStage, savedPet.getStage());
        return new PetResponse(savedPet);*/
    }

    public void deletePet(Long petId) {
        User currentUser = getCurrentUser();
        Pet pet = petRepository.findByIdAndOwner(petId, currentUser)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        logger.info("Deleting pet '{}' from user: {}", pet.getName(), currentUser.getUserName());
        petRepository.delete(pet);
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}