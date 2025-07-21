package com.toni.virtualpet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toni.virtualpet.dto.request.CreatePetRequest;
import com.toni.virtualpet.dto.request.LoginRequest;
import com.toni.virtualpet.dto.request.RegisterRequest;
import com.toni.virtualpet.model.enums.Variant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
class VirtualPetApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String jwtToken;
    private static Long petId;

    @Test
    @Order(1)
    void contextLoads() {
    }

    @Test
    @Order(2)
    void completeUserJourney_RegisterLoginCreatePetAndInteract() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("testuser@example.com", "testuser", "password123");

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("testuser"));

        LoginRequest loginRequest = new LoginRequest("testuser", "password123");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").exists())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        jwtToken = extractTokenFromResponse(responseBody);

        mockMvc.perform(get("/api/auth/verify")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        CreatePetRequest createPetRequest = new CreatePetRequest("Draco", Variant.MOUNTAIN);

        MvcResult createPetResult = mockMvc.perform(post("/api/pets")
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPetRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Draco"))
                .andExpect(jsonPath("$.data.variant").value("MOUNTAIN"))
                .andExpect(jsonPath("$.data.stage").value("EGG"))
                .andReturn();

        petId = extractPetIdFromResponse(createPetResult.getResponse().getContentAsString());

        mockMvc.perform(get("/api/pets")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Draco"));

        mockMvc.perform(get("/api/pets/" + petId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(petId))
                .andExpect(jsonPath("$.data.name").value("Draco"));

        mockMvc.perform(post("/api/pets/" + petId + "/feed")
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Dragon fed successfully! 🍖"));

        mockMvc.perform(post("/api/pets/" + petId + "/play")
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Played with dragon! 🎾"));

        mockMvc.perform(post("/api/pets/" + petId + "/rest")
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Dragon is resting! 💤"));

        mockMvc.perform(post("/api/pets/" + petId + "/evolve")
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void unauthorizedAccess_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isUnauthorized());

        CreatePetRequest request = new CreatePetRequest("Unauthorized", Variant.FOREST);
        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(4)
    void adminEndpoints_RequireAdminRole() throws Exception {
        mockMvc.perform(get("/api/admin/pets")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/admin/users")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(5)
    void cleanup_DeleteCreatedPet() throws Exception {
        if (petId != null && jwtToken != null) {
            mockMvc.perform(delete("/api/pets/" + petId)
                            .header("Authorization", "Bearer " + jwtToken)
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Dragon released to the wild"));
        }
    }

    private String extractTokenFromResponse(String responseBody) {
        try {
            com.fasterxml.jackson.databind.JsonNode node = objectMapper.readTree(responseBody);
            return node.get("data").get("token").asText();
        } catch (Exception e) {
            return "fake-token-for-test";
        }
    }

    private Long extractPetIdFromResponse(String responseBody) {
        try {
            com.fasterxml.jackson.databind.JsonNode node = objectMapper.readTree(responseBody);
            return node.get("data").get("id").asLong();
        } catch (Exception e) {
            return 1L;
        }
    }
}