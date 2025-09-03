package com.toni.virtualpet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toni.virtualpet.dto.request.CreatePetRequest;
import com.toni.virtualpet.model.pet.enums.Variant;
import com.toni.virtualpet.model.user.User;
import com.toni.virtualpet.model.user.enums.Role;
import com.toni.virtualpet.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user = User.builder()
                .username("toni")
                .email("toni@example.com")
                .password(passwordEncoder.encode("password123"))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
    }

    @Test
    @WithMockUser(username = "toni", roles = "USER")
    void createPet_ReturnsCreatedPet() throws Exception {
        CreatePetRequest request = new CreatePetRequest("Draco", Variant.MOUNTAIN);

        mockMvc.perform(post("/api/pets")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Pet created successfully"))
                .andExpect(jsonPath("$.data.name").value("Draco"))
                .andExpect(jsonPath("$.data.variant").value("MOUNTAIN"));
    }

    @Test
    @WithMockUser(username = "toni", roles = "USER")
    void getUserPets_ReturnsListOfPets() throws Exception {
        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void getUserPets_WithoutAuthentication_ReturnsForbidden() throws Exception {
        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isUnauthorized());
    }
}