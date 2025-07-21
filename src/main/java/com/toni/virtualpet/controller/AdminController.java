package com.toni.virtualpet.controller;

import com.toni.virtualpet.dto.response.ApiResponse;
import com.toni.virtualpet.dto.response.PetResponse;
import com.toni.virtualpet.dto.response.UserResponse;
import com.toni.virtualpet.model.User;
import com.toni.virtualpet.model.enums.Stage;
import com.toni.virtualpet.model.enums.Variant;
import com.toni.virtualpet.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    @GetMapping("/pets")
    public ResponseEntity<ApiResponse<List<PetResponse>>> getAllPets() {
        logger.info("Admin getting all pets from system");

        List<PetResponse> pets = adminService.getAllPets();

        logger.info("Retrieved {} pets for admin", pets.size());
        return ResponseEntity.ok(
                ApiResponse.success("All pets retrieved", pets)
        );
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        logger.info("Admin getting all users from system");

        List<User> users = adminService.getAllUsers();
        List<UserResponse> userResponses = users.stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());

        logger.info("Retrieved {} users for admin", userResponses.size());
        return ResponseEntity.ok(
                ApiResponse.success("All users retrieved", userResponses)
        );
    }

    @GetMapping("/pets/{petId}")
    public ResponseEntity<ApiResponse<PetResponse>> getPetById(@PathVariable Long petId) {
        logger.info("Admin getting pet by ID: {}", petId);

        PetResponse petResponse = adminService.getPetById(petId);

        logger.info("Admin retrieved pet: {}", petResponse.getName());
        return ResponseEntity.ok(
                ApiResponse.success("Pet retrieved", petResponse)
        );
    }

    @DeleteMapping("/pets/{petId}")
    public ResponseEntity<ApiResponse<Void>> deletePetById(@PathVariable Long petId) {
        logger.info("Admin deleting pet by ID: {}", petId);

        adminService.deletePetById(petId);

        logger.info("Admin deleted pet: {}", petId);
        return ResponseEntity.ok(
                ApiResponse.success("Pet deleted by admin", null)
        );
    }

    @GetMapping("/users/{userId}/pets")
    public ResponseEntity<ApiResponse<List<PetResponse>>> getPetsByUser(@PathVariable Long userId) {
        logger.info("Admin getting pets from user ID: {}", userId);

        List<PetResponse> pets = adminService.getPetsByUser(userId);

        logger.info("Retrieved {} pets for user {} (admin view)", pets.size(), userId);
        return ResponseEntity.ok(
                ApiResponse.success("User pets retrieved", pets)
        );
    }

    @GetMapping("/pets/variant/{variant}")
    public ResponseEntity<ApiResponse<List<PetResponse>>> getPetsByVariant(@PathVariable Variant variant) {
        logger.info("Admin getting pets by variant: {}", variant);

        List<PetResponse> pets = adminService.getPetsByVariant(variant);

        logger.info("Retrieved {} {} dragons", pets.size(), variant);
        return ResponseEntity.ok(
                ApiResponse.success(variant + " dragons retrieved", pets)
        );
    }

    @GetMapping("/pets/stage/{stage}")
    public ResponseEntity<ApiResponse<List<PetResponse>>> getPetsByStage(@PathVariable Stage stage) {
        logger.info("Admin getting pets by stage: {}", stage);

        List<PetResponse> pets = adminService.getPetsByStage(stage);

        logger.info("Retrieved {} {} stage dragons", pets.size(), stage);
        return ResponseEntity.ok(
                ApiResponse.success(stage + " stage dragons retrieved", pets)
        );
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<AdminDashboard>> getDashboard() {
        logger.info("Admin getting dashboard statistics");

        List<PetResponse> allPets = adminService.getAllPets();
        List<User> allUsers = adminService.getAllUsers();

        AdminDashboard dashboard = new AdminDashboard(
                allUsers.size(),
                allPets.size(),
                adminService.getPetsByVariant(Variant.MOUNTAIN).size(),
                adminService.getPetsByVariant(Variant.SWAMP).size(),
                adminService.getPetsByVariant(Variant.FOREST).size(),
                adminService.getPetsByStage(Stage.EGG).size(),
                adminService.getPetsByStage(Stage.YOUNG).size(),
                adminService.getPetsByStage(Stage.ADULT).size(),
                adminService.getPetsByStage(Stage.ANCIENT).size()
        );

        logger.info("Dashboard stats: {} users, {} pets", dashboard.totalUsers(), dashboard.totalPets());
        return ResponseEntity.ok(
                ApiResponse.success("Dashboard statistics", dashboard)
        );
    }

    public record AdminDashboard(
            int totalUsers,
            int totalPets,
            int mountainDragons,
            int swampDragons,
            int forestDragons,
            int eggStage,
            int youngStage,
            int adultStage,
            int ancientStage
    ) {}
}