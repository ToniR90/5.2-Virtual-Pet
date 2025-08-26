package com.toni.virtualpet.service.pet;

import com.toni.virtualpet.dto.request.CreatePetRequest;
import com.toni.virtualpet.dto.response.PetResponse;
import com.toni.virtualpet.exception.personalizedException.PetActionException;
import com.toni.virtualpet.exception.personalizedException.PetNotFoundException;
import com.toni.virtualpet.exception.personalizedException.UserNotFoundException;
import com.toni.virtualpet.model.pet.Pet;
import com.toni.virtualpet.model.petAction.PetAction;
import com.toni.virtualpet.model.user.User;
import com.toni.virtualpet.model.petAction.enums.ActionType;
import com.toni.virtualpet.model.pet.enums.Stage;
import com.toni.virtualpet.repository.PetActionRepository;
import com.toni.virtualpet.repository.PetRepository;
import com.toni.virtualpet.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
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
        logger.info("Creating Pet '{}' variant {} for user: {}" , request.getName() , request.getVariant() , currentUser.getUsername());

        Pet pet = Pet.builder()
                .name(request.getName())
                .variant(request.getVariant())
                .owner(currentUser)
                .stage(Stage.EGG)
                .experience(0)
                .energy(50)
                .happiness(50)
                .hunger(50)
                .build();
        Pet savedPet = petRepository.save(pet);

        logger.info("Pet created: {}", savedPet);
        return PetResponse.from(savedPet);
    }

    public List<PetResponse> getUserPets() {
        User currentUser = getCurrentUser();
        logger.info("Getting pets from user: {}" , currentUser.getUsername());

        List<Pet> pets = petRepository.findByOwner(currentUser);
        return pets.stream()
                .map(PetResponse::from)
                .collect(Collectors.toList());
    }

    public PetResponse getPetById(Long petId) {
        User currentUser = getCurrentUser();
        Pet pet = petRepository.findByIdAndOwner(petId, currentUser)
                .stream()
                .findFirst()
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));

        return PetResponse.from(pet);
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
                .stream()
                .findFirst()
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));

        logger.info("Action in progress '{}' on pet '{}' from user: {}",
                actionName, pet.getName(), currentUser.getUsername());

        switch(actionType) {
            case FEED -> pet.feed();
            case PLAY -> pet.play();
            case REST -> pet.rest();
            case IGNORE -> pet.ignore();
        }

        Pet savedPet = petRepository.save(pet);
        PetAction action = new PetAction();
        action.setActionType(actionType);
        action.setPet(savedPet);
        action.setUser(currentUser);
        petActionRepository.save(action);

        logger.info("Action '{}' done. Pet updated: {}", actionName, savedPet);
        return PetResponse.from(savedPet);
    }

    public PetResponse evolvePet(Long petId) {
        User currentUser = getCurrentUser();
        Pet pet = petRepository.findByIdAndOwner(petId, currentUser)
                .stream()
                .findFirst()
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));

        if (!pet.canEvolve()) {
            throw PetActionException.cannotEvolve(pet.getName());
        }

        logger.info("Evolving Pet '{}' from user: {}", pet.getName(), currentUser.getUsername());

        Stage oldStage = pet.getStage();
        pet.evolve();

        Pet savedPet = petRepository.save(pet);

        PetAction action = new PetAction();
        action.setActionType(ActionType.EVOLVE);
        action.setPet(savedPet);
        action.setUser(currentUser);

        petActionRepository.save(action);

        logger.info("Pet evolved from {} to {}", oldStage, savedPet.getStage());
        return PetResponse.from(savedPet);
    }

    public void deletePet(Long petId) {
        User currentUser = getCurrentUser();
        Pet pet = petRepository.findByIdAndOwner(petId, currentUser)
                .stream()
                .findFirst()
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));

        logger.info("Deleting pet '{}' from user: {}", pet.getName(), currentUser.getUsername());
        petRepository.delete(pet);
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}