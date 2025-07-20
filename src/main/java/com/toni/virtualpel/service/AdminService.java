package com.toni.virtualpel.service;

import com.toni.virtualpel.dto.response.PetResponse;
import com.toni.virtualpel.model.Pet;
import com.toni.virtualpel.model.User;
import com.toni.virtualpel.model.enums.Stage;
import com.toni.virtualpel.model.enums.Variant;
import com.toni.virtualpel.repository.PetRepository;
import com.toni.virtualpel.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public List<PetResponse> getAllPets() {
        logger.info("Admin getting all pets from system");

        List<Pet> pets = petRepository.findAll();
        return pets.stream()
                .map(PetResponse::new)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        logger.info("Admin getting all users from system");
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PetResponse getPetById(Long petId) {
        logger.info("Admin getting pet by ID: {}", petId);

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        return new PetResponse(pet);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deletePetById(Long petId) {
        logger.info("Admin deleting pet by ID: {}", petId);

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        petRepository.delete(pet);
        logger.info("Pet deleted by admin: {}", pet.getName());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<PetResponse> getPetsByUser(Long userId) {
        logger.info("Admin obteniendo mascotas del usuario con ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Pet> pets = petRepository.findByOwner(user);
        return pets.stream()
                .map(PetResponse::new)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<PetResponse> getPetsByVariant(Variant variant) {
        logger.info("Admin getting pets by variant: {}", variant);

        List<Pet> pets = petRepository.findByVariant(variant);
        return pets.stream()
                .map(PetResponse::new)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<PetResponse> getPetsByStage(Stage stage) {
        logger.info("Admin getting pets by stage: {}", stage);

        List<Pet> pets = petRepository.findByStage(stage);
        return pets.stream()
                .map(PetResponse::new)
                .collect(Collectors.toList());
    }
}
