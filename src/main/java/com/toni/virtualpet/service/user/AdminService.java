package com.toni.virtualpet.service.user;

import com.toni.virtualpet.dto.response.PetResponse;
import com.toni.virtualpet.dto.response.UserResponse;
import com.toni.virtualpet.exception.personalizedException.PetNotFoundException;
import com.toni.virtualpet.exception.personalizedException.UserNotFoundException;
import com.toni.virtualpet.model.pet.Pet;
import com.toni.virtualpet.model.user.User;
import com.toni.virtualpet.model.pet.enums.Stage;
import com.toni.virtualpet.model.pet.enums.Variant;
import com.toni.virtualpet.model.user.enums.Role;
import com.toni.virtualpet.repository.PetRepository;
import com.toni.virtualpet.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    private PetRepository petRepository;
    private UserRepository userRepository;

    public List<PetResponse> getAllPets() {
        logger.info("Admin getting all pets from system");

        List<Pet> pets = petRepository.findAll();
        return pets.stream()
                .map(PetResponse::from)
                .collect(Collectors.toList());
    }

    public List<User> getAllUsers() {
        logger.info("Admin getting all users from system");
        return userRepository.findAll();
    }

    public PetResponse getPetById(Long petId) {
        logger.info("Admin getting pet by ID: {}", petId);

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));

        return PetResponse.from(pet);
    }

    public void deletePetById(Long petId) {
        logger.info("Admin deleting pet by ID: {}", petId);

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));

        petRepository.delete(pet);
        logger.info("Pet deleted by admin: {}", pet.getName());
    }

    public List<PetResponse> getPetsByUser(Long userId) {
        logger.info("Admin getting pets from user's ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Pet> pets = petRepository.findByOwner(user);
        return pets.stream()
                .map(PetResponse::from)
                .collect(Collectors.toList());
    }

    public List<PetResponse> getPetsByVariant(Variant variant) {
        logger.info("Admin getting pets by variant: {}", variant);

        List<Pet> pets = petRepository.findByVariant(variant);
        return pets.stream()
                .map(PetResponse::from)
                .collect(Collectors.toList());
    }

    public List<PetResponse> getPetsByStage(Stage stage) {
        logger.info("Admin getting pets by stage: {}", stage);

        List<Pet> pets = petRepository.findByStage(stage);
        return pets.stream()
                .map(PetResponse::from)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return UserResponse.from(user);
    }

    public List<User> getUserByRole(Role role) {
        return userRepository.findByRole(role);
    }
}