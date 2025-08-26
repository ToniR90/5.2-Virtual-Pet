package com.toni.virtualpet.service;

import com.toni.virtualpet.dto.request.CreatePetRequest;
import com.toni.virtualpet.dto.response.PetResponse;
import com.toni.virtualpet.exception.PetNotFoundException;
import com.toni.virtualpet.exception.UserNotFoundException;
import com.toni.virtualpet.model.pet.Pet;
import com.toni.virtualpet.model.user.User;
import com.toni.virtualpet.model.user.enums.Role;
import com.toni.virtualpet.model.pet.enums.Stage;
import com.toni.virtualpet.model.pet.enums.Variant;
import com.toni.virtualpet.repository.PetActionRepository;
import com.toni.virtualpet.repository.PetRepository;
import com.toni.virtualpet.repository.UserRepository;
import com.toni.virtualpet.service.pet.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PetActionRepository petActionRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PetService petService;

    private User testUser;
    private Pet testPet;
    private CreatePetRequest createPetRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("hashedPassword")
                .role(Role.ROLE_USER)
                .build();

        testPet = Pet.builder()
                .id(1L)
                .name("Draco")
                .variant(Variant.MOUNTAIN)
                .stage(Stage.EGG)
                .experience(0)
                .energy(50)
                .happiness(50)
                .hunger(50)
                .owner(testUser)
                .build();

        createPetRequest = new CreatePetRequest("Draco", Variant.MOUNTAIN);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
    }

    @Test
    void createPet_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(petRepository.save(any(Pet.class))).thenReturn(testPet);

        PetResponse result = petService.createPet(createPetRequest);

        assertNotNull(result);
        assertEquals("Draco", result.getName());
        assertEquals("MOUNTAIN", result.getVariant());
        assertEquals("EGG", result.getStage());
        assertEquals(0, result.getExperience());

        verify(petRepository).save(any(Pet.class));
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void createPet_UserNotFound_ThrowsException() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            petService.createPet(createPetRequest);
        });

        verify(petRepository, never()).save(any());
    }

    @Test
    void getUserPets_Success() {
        List<Pet> pets = Arrays.asList(testPet);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(petRepository.findByOwner(testUser)).thenReturn(pets);

        List<PetResponse> result = petService.getUserPets();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Draco", result.get(0).getName());

        verify(petRepository).findByOwner(testUser);
    }

    @Test
    void getPetById_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(petRepository.findByIdAndOwner(1L, testUser)).thenReturn(Arrays.asList(testPet));

        PetResponse result = petService.getPetById(1L);

        assertNotNull(result);
        assertEquals("Draco", result.getName());
        assertEquals(1L, result.getId());

        verify(petRepository).findByIdAndOwner(1L, testUser);
    }

    @Test
    void getPetById_PetNotFound_ThrowsException() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(petRepository.findByIdAndOwner(1L, testUser)).thenReturn(Arrays.asList());

        assertThrows(PetNotFoundException.class, () -> {
            petService.getPetById(1L);
        });
    }

    @Test
    void feedPet_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(petRepository.findByIdAndOwner(1L, testUser)).thenReturn(Arrays.asList(testPet));
        when(petRepository.save(any(Pet.class))).thenReturn(testPet);

        PetResponse result = petService.feedPet(1L);

        assertNotNull(result);
        verify(petRepository).save(any(Pet.class));
        verify(petActionRepository).save(any());
    }

    @Test
    void playWithPet_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(petRepository.findByIdAndOwner(1L, testUser)).thenReturn(Arrays.asList(testPet));
        when(petRepository.save(any(Pet.class))).thenReturn(testPet);

        PetResponse result = petService.playWithPet(1L);

        assertNotNull(result);
        verify(petRepository).save(any(Pet.class));
        verify(petActionRepository).save(any());
    }

    @Test
    void evolvePet_Success() {
        testPet.setExperience(3);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(petRepository.findByIdAndOwner(1L, testUser)).thenReturn(Arrays.asList(testPet));
        when(petRepository.save(any(Pet.class))).thenReturn(testPet);

        PetResponse result = petService.evolvePet(1L);

        assertNotNull(result);
        verify(petRepository).save(any(Pet.class));
        verify(petActionRepository).save(any());
    }

    @Test
    void evolvePet_CannotEvolve_ThrowsException() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(petRepository.findByIdAndOwner(1L, testUser)).thenReturn(Arrays.asList(testPet));

        assertThrows(RuntimeException.class, () -> {
            petService.evolvePet(1L);
        });

        verify(petRepository, never()).save(any());
    }

    @Test
    void deletePet_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(petRepository.findByIdAndOwner(1L, testUser)).thenReturn(Arrays.asList(testPet));

        petService.deletePet(1L);

        verify(petRepository).delete(testPet);
    }
}