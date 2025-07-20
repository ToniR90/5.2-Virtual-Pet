package com.toni.virtualpel.controller;

import com.toni.virtualpel.dto.response.ApiResponse;
import com.toni.virtualpel.dto.response.PetResponse;
import com.toni.virtualpel.model.User;
import com.toni.virtualpel.service.AdminService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    @GetMapping("/pets")
    public ResponseEntity<?> getAllPets() {
        try {
            logger.info("Admin obteniendo todas las mascotas");

            List<PetResponse> pets = adminService.getAllPets();

            return ResponseEntity.ok(new ApiResponse<>(true, "Todas las mascotas obtenidas exitosamente", pets));
        } catch (Exception e) {
            logger.error("Error obteniendo todas las mascotas: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            logger.info("Admin obteniendo todos los usuarios");

            List<User> users = adminService.getAllUsers();

            return ResponseEntity.ok(new ApiResponse<>(true, "Todos los usuarios obtenidos exitosamente", users));
        } catch (Exception e) {
            logger.error("Error obteniendo todos los usuarios: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage()));
        }
    }

    @GetMapping("/pets/{petId}")
    public ResponseEntity<?> getPetById(@PathVariable Long petId) {
        try {
            logger.info("Admin obteniendo mascota con ID: {}", petId);

            PetResponse petResponse = adminService.getPetById(petId);

            return ResponseEntity.ok(new ApiResponse<>(true, "Mascota obtenida exitosamente", petResponse));
        } catch (Exception e) {
            logger.error("Error obteniendo mascota {}: {}", petId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage()));
        }
    }

    @DeleteMapping("/pets/{petId}")
    public ResponseEntity<?> deletePetById(@PathVariable Long petId) {
        try {
            logger.info("Admin eliminando mascota con ID: {}", petId);

            adminService.deletePetById(petId);

            return ResponseEntity.ok(new ApiResponse<>(true, "Mascota eliminada exitosamente"));
        } catch (Exception e) {
            logger.error("Error eliminando mascota {}: {}", petId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage()));
        }
    }

    @GetMapping("/users/{userId}/pets")
    public ResponseEntity<?> getPetsByUser(@PathVariable Long userId) {
        try {
            logger.info("Admin obteniendo mascotas del usuario con ID: {}", userId);

            List<PetResponse> pets = adminService.getPetsByUser(userId);

            return ResponseEntity.ok(new ApiResponse<>(true, "Mascotas del usuario obtenidas exitosamente", pets));
        } catch (Exception e) {
            logger.error("Error obteniendo mascotas del usuario {}: {}", userId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage()));
        }
    }
}