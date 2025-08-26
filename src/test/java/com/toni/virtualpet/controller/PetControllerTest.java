package com.toni.virtualpet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toni.virtualpet.dto.request.CreatePetRequest;
import com.toni.virtualpet.dto.response.PetResponse;
import com.toni.virtualpet.model.pet.enums.Variant;
import com.toni.virtualpet.security.JwtRequestFilter;
import com.toni.virtualpet.security.JwtUtils;
import com.toni.virtualpet.service.pet.PetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PetService petService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void createPet_Success() throws Exception {
        CreatePetRequest request = new CreatePetRequest("Draco", Variant.MOUNTAIN);
        PetResponse petResponse = createMockPetResponse(1L, "Draco", "MOUNTAIN");

        when(petService.createPet(any(CreatePetRequest.class))).thenReturn(petResponse);

        mockMvc.perform(post("/api/pets")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Dragon created successfully"))
                .andExpect(jsonPath("$.data.name").value("Draco"))
                .andExpect(jsonPath("$.data.variant").value("MOUNTAIN"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void createPet_InvalidInput_BadRequest() throws Exception {
        CreatePetRequest request = new CreatePetRequest("", Variant.MOUNTAIN);

        mockMvc.perform(post("/api/pets")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void getUserPets_Success() throws Exception {
        List<PetResponse> pets = Arrays.asList(
                createMockPetResponse(1L, "Draco", "MOUNTAIN"),
                createMockPetResponse(2L, "Shadow", "SWAMP")
        );

        when(petService.getUserPets()).thenReturn(pets);

        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].name").value("Draco"))
                .andExpect(jsonPath("$.data[1].name").value("Shadow"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void getPetById_Success() throws Exception {
        PetResponse petResponse = createMockPetResponse(1L, "Draco", "MOUNTAIN");
        when(petService.getPetById(1L)).thenReturn(petResponse);

        mockMvc.perform(get("/api/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Draco"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void feedPet_Success() throws Exception {
        PetResponse petResponse = createMockPetResponse(1L, "Draco", "MOUNTAIN");
        when(petService.feedPet(1L)).thenReturn(petResponse);

        mockMvc.perform(post("/api/pets/1/feed")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Dragon fed successfully! 🍖"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void playWithPet_Success() throws Exception {
        PetResponse petResponse = createMockPetResponse(1L, "Draco", "MOUNTAIN");
        when(petService.playWithPet(1L)).thenReturn(petResponse);

        mockMvc.perform(post("/api/pets/1/play")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Played with dragon! 🎾"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void evolvePet_Success() throws Exception {
        PetResponse petResponse = createMockPetResponse(1L, "Draco", "MOUNTAIN");
        when(petService.evolvePet(1L)).thenReturn(petResponse);

        mockMvc.perform(post("/api/pets/1/evolve")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Dragon evolved! ✨"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void deletePet_Success() throws Exception {
        mockMvc.perform(delete("/api/pets/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Dragon released to the wild"));
    }

    @Test
    void createPet_Unauthorized_ReturnsUnauthorized() throws Exception {
        CreatePetRequest request = new CreatePetRequest("Draco", Variant.MOUNTAIN);

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    private PetResponse createMockPetResponse(Long id, String name, String variant) {
        PetResponse response = new PetResponse();
        return response;
    }
}