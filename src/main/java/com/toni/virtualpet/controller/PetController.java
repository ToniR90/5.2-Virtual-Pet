package com.toni.virtualpet.controller;

import com.toni.virtualpet.dto.request.CreatePetRequest;
import com.toni.virtualpet.dto.response.ApiResponse;
import com.toni.virtualpet.dto.response.PetResponse;
import com.toni.virtualpet.service.PetService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PetController {

    private static final Logger logger = LoggerFactory.getLogger(PetController.class);

    @Autowired
    private PetService petService;

    @PostMapping
    public ResponseEntity<ApiResponse<PetResponse>> createPet(@Valid @RequestBody CreatePetRequest request) {
        logger.info("Creating new pet: {}", request.getName());

        PetResponse petResponse = petService.createPet(request);

        logger.info("Pet created successfully: {}", petResponse.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Dragon created successfully", petResponse));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PetResponse>>> getUserPets() {
        logger.info("Getting user's pets");

        List<PetResponse> pets = petService.getUserPets();

        logger.info("Retrieved {} pets for user", pets.size());
        return ResponseEntity.ok(
                ApiResponse.success("Pets retrieved successfully", pets)
        );
    }

    @GetMapping("/{petId}")
    public ResponseEntity<ApiResponse<PetResponse>> getPetById(@PathVariable Long petId) {
        logger.info("Getting pet by ID: {}", petId);

        PetResponse petResponse = petService.getPetById(petId);

        logger.info("Pet retrieved: {}", petResponse.getName());
        return ResponseEntity.ok(
                ApiResponse.success("Pet retrieved successfully", petResponse)
        );
    }

    @PostMapping("/{petId}/feed")
    public ResponseEntity<ApiResponse<PetResponse>> feedPet(@PathVariable Long petId) {
        logger.info("Feeding pet: {}", petId);

        PetResponse petResponse = petService.feedPet(petId);

        logger.info("Pet fed successfully: {}", petResponse.getName());
        return ResponseEntity.ok(
                ApiResponse.success("Dragon fed successfully! 🍖", petResponse)
        );
    }

    @PostMapping("/{petId}/play")
    public ResponseEntity<ApiResponse<PetResponse>> playWithPet(@PathVariable Long petId) {
        logger.info("Playing with pet: {}", petId);

        PetResponse petResponse = petService.playWithPet(petId);

        logger.info("Played with pet successfully: {}", petResponse.getName());
        return ResponseEntity.ok(
                ApiResponse.success("Played with dragon! 🎾", petResponse)
        );
    }

    @PostMapping("/{petId}/rest")
    public ResponseEntity<ApiResponse<PetResponse>> restPet(@PathVariable Long petId) {
        logger.info("Resting pet: {}", petId);

        PetResponse petResponse = petService.restPet(petId);

        logger.info("Pet rested successfully: {}", petResponse.getName());
        return ResponseEntity.ok(
                ApiResponse.success("Dragon is resting! 💤", petResponse)
        );
    }

    @PostMapping("/{petId}/ignore")
    public ResponseEntity<ApiResponse<PetResponse>> ignorePet(@PathVariable Long petId) {
        logger.info("Ignoring pet: {}", petId);

        PetResponse petResponse = petService.ignorePet(petId);

        logger.info("Pet ignored: {}", petResponse.getName());
        return ResponseEntity.ok(
                ApiResponse.success("Dragon ignored... 😴", petResponse)
        );
    }

    @PostMapping("/{petId}/evolve")
    public ResponseEntity<ApiResponse<PetResponse>> evolvePet(@PathVariable Long petId) {
        logger.info("Evolving pet: {}", petId);

        PetResponse petResponse = petService.evolvePet(petId);

        logger.info("Pet evolved successfully: {} -> {}", petResponse.getName(), petResponse.getStage());
        return ResponseEntity.ok(
                ApiResponse.success("Dragon evolved! ✨", petResponse)
        );
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<ApiResponse<Void>> deletePet(@PathVariable Long petId) {
        logger.info("Deleting pet: {}", petId);

        petService.deletePet(petId);

        logger.info("Pet deleted successfully: {}", petId);
        return ResponseEntity.ok(
                ApiResponse.success("Dragon released to the wild", null)
        );
    }
}