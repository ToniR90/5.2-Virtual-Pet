package com.toni.virtualpel.controller;

import com.toni.virtualpel.dto.response.ApiResponse;
import com.toni.virtualpel.dto.request.CreatePetRequest;
import com.toni.virtualpel.dto.response.PetResponse;
import com.toni.virtualpel.service.PetService;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/pets")
@CrossOrigin(origins = "*")
public class PetController {
    private static final Logger logger = LoggerFactory.getLogger(PetController.class);

    @Autowired
    private PetService petService;

    @PostMapping
    public ResponseEntity<?> createPet(@Valid @RequestBody CreatePetRequest request) {
        try {
            logger.info("Solicitud de creación de mascota: {} variante {}",
                    request.getName(), request.getVariant());

            PetResponse petResponse = petService.createPet(request);

            return ResponseEntity.ok(new ApiResponse<>(true, "Mascota creada exitosamente", petResponse));
        } catch (Exception e) {
            logger.error("Error creando mascota: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserPets() {
        try {
            logger.info("Obteniendo mascotas del usuario");

            List<PetResponse> pets = petService.getUserPets();

            return ResponseEntity.ok(new ApiResponse<>(true, "Mascotas obtenidas exitosamente", pets));
        } catch (Exception e) {
            logger.error("Error obteniendo mascotas: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage()));
        }
    }

    @GetMapping("/{petId}")
    public ResponseEntity<?> getPet(@PathVariable Long petId) {
        try {
            logger.info("Obteniendo mascota con ID: {}", petId);

            PetResponse petResponse = petService.getPetById(petId);

            return ResponseEntity.ok(new ApiResponse<>(true, "Mascota obtenida exitosamente", petResponse));
        } catch (Exception e) {
            logger.error("Error obteniendo mascota {}: {}", petId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage()));
        }
    }

    @PostMapping("/{petId}/feed")
    public ResponseEntity<?> feedPet(@PathVariable Long petId) {
        try {
            logger.info("Alimentando mascota con ID: {}", petId);

            PetResponse petResponse = petService.feedPet(petId);

            return ResponseEntity.ok(new ApiResponse<>(true, "Mascota alimentada exitosamente", petResponse));
        } catch (Exception e) {
            logger.error("Error alimentando mascota {}: {}", petId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage()));
        }
    }

    @PostMapping("/{petId}/play")
    public ResponseEntity<?> playWithPet(@PathVariable Long petId) {
        try {
            logger.info("Jugando con mascota con ID: {}", petId);

            PetResponse petResponse = petService.playWithPet(petId);

            return ResponseEntity.ok(new ApiResponse<>(true, "Jugaste con la mascota exitosamente", petResponse));
        } catch (Exception e) {
            logger.error("Error jugando con mascota {}: {}", petId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage()));
        }
    }

    @PostMapping("/{petId}/rest")
    public ResponseEntity<?> restPet(@PathVariable Long petId) {
        try {
            logger.info("Mascota descansando con ID: {}", petId);

            PetResponse petResponse = petService.restPet(petId);

            return ResponseEntity.ok(new ApiResponse<>(true, "La mascota descansó exitosamente", petResponse));
        } catch (Exception e) {
            logger.error("Error descansando mascota {}: {}", petId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage()));
        }
    }

    @PostMapping("/{petId}/ignore")
    public ResponseEntity<?> ignorePet(@PathVariable Long petId) {
        try {
            logger.info("Ignorando mascota con ID: {}", petId);

            PetResponse petResponse = petService.ignorePet(petId);

            return ResponseEntity.ok(new ApiResponse<>(true, "Ignoraste a la mascota", petResponse));
        } catch (Exception e) {
            logger.error("Error ignorando mascota {}: {}", petId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage()));
        }
    }

    @PostMapping("/{petId}/evolve")
    public ResponseEntity<?> evolvePet(@PathVariable Long petId) {
        try {
            logger.info("Evolucionando mascota con ID: {}", petId);

            PetResponse petResponse = petService.evolvePet(petId);

            return ResponseEntity.ok(new ApiResponse<>(true, "Mascota evolucionada exitosamente", petResponse));
        } catch (Exception e) {
            logger.error("Error evolucionando mascota {}: {}", petId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<?> deletePet(@PathVariable Long petId) {
        try {
            logger.info("Eliminando mascota con ID: {}", petId);

            petService.deletePet(petId);

            return ResponseEntity.ok(new ApiResponse<>(true, "Mascota eliminada exitosamente"));
        } catch (Exception e) {
            logger.error("Error eliminando mascota {}: {}", petId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage()));
        }
    }
}