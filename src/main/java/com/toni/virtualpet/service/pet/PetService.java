package com.toni.virtualpet.service.pet;

import com.toni.virtualpet.dto.request.CreatePetRequest;
import com.toni.virtualpet.dto.response.PetResponse;
import com.toni.virtualpet.exception.personalizedException.PetNotFoundException;
import com.toni.virtualpet.model.pet.Pet;
import com.toni.virtualpet.model.petAction.enums.ActionType;
import com.toni.virtualpet.model.user.User;
import com.toni.virtualpet.model.pet.enums.Stage;
import com.toni.virtualpet.repository.PetRepository;
import com.toni.virtualpet.service.petAction.PetActionService;
import com.toni.virtualpet.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class PetService {

    private static final Logger logger = LoggerFactory.getLogger(PetService.class);

    private PetRepository petRepository;
    private PetActionService petActionService;
    private UserService userService;

    public PetResponse createPet(CreatePetRequest request) {
        User currentUser = userService.getCurrentUser();
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

    public long countPetsByOwner(Long id) {
        return petRepository.countPetsByOwnerId(id);
    }

    public List<PetResponse> getUserPets() {
        User currentUser = userService.getCurrentUser();
        logger.info("Getting pets from user: {}" , currentUser.getUsername());

        List<Pet> pets = petRepository.findByOwner(currentUser);
        return pets.stream()
                .map(PetResponse::from)
                .collect(Collectors.toList());
    }

    public PetResponse getPetById(Long petId) {
        User currentUser = userService.getCurrentUser();
        Pet pet = petRepository.findByIdAndOwner(petId, currentUser)
                .stream()
                .findFirst()
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));

        return PetResponse.from(pet);
    }

    public void deletePet(Long petId) {
        User currentUser = userService.getCurrentUser();
        Pet pet = petRepository.findByIdAndOwner(petId, currentUser)
                .stream()
                .findFirst()
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));

        logger.info("Deleting pet '{}' from user: {}", pet.getName(), currentUser.getUsername());
        petRepository.delete(pet);
    }

    public PetResponse feedPet(Long petId) {
        return applyActionToPet(petId, ActionType.FEED);
    }

    public PetResponse playWithPet(Long petId) {
        return applyActionToPet(petId, ActionType.PLAY);
    }

    public PetResponse restPet(Long petId) {
        return applyActionToPet(petId, ActionType.REST);
    }

    public PetResponse ignorePet(Long petId) {
        return applyActionToPet(petId, ActionType.IGNORE);
    }

    public PetResponse evolvePet(Long petId) {
        return applyActionToPet(petId, ActionType.EVOLVE);
    }

    private PetResponse applyActionToPet(Long petId, ActionType actionType) {
        User user = userService.getCurrentUser();
        Pet pet = petRepository.findByIdAndOwner(petId, user)
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));

        Pet updatedPet = petActionService.applyAction(pet, user, actionType);
        return PetResponse.from(petRepository.save(updatedPet));
    }

}