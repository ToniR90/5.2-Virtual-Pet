package com.toni.virtualpet.service.pet;

import com.toni.virtualpet.dto.request.CreatePetRequest;
import com.toni.virtualpet.dto.response.PetResponse;
import com.toni.virtualpet.exception.personalizedException.PetNotFoundException;
import com.toni.virtualpet.model.pet.Pet;
import com.toni.virtualpet.model.pet.enums.Stage;
import com.toni.virtualpet.model.pet.enums.Variant;
import com.toni.virtualpet.model.petAction.enums.ActionType;
import com.toni.virtualpet.model.user.User;
import com.toni.virtualpet.repository.PetRepository;
import com.toni.virtualpet.service.petAction.PetActionService;
import com.toni.virtualpet.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetServiceTest {

    private PetRepository petRepository;
    private PetActionService petActionService;
    private UserService userService;
    private PetService petService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        petRepository = mock(PetRepository.class);
        petActionService = mock(PetActionService.class);
        userService = mock(UserService.class);

        petService = new PetService(petRepository, petActionService, userService);

        mockUser = User.builder()
                .id(1L)
                .username("toni")
                .email("toni@example.com")
                .build();

        when(userService.getCurrentUser()).thenReturn(mockUser);
    }

    @Test
    void createPet_ReturnsPetResponse() {
        CreatePetRequest request = new CreatePetRequest("Fluffy", Variant.MOUNTAIN);

        Pet pet = Pet.builder()
                .id(1L)
                .name("Fluffy")
                .variant(Variant.MOUNTAIN)
                .owner(mockUser)
                .stage(Stage.EGG)
                .experience(0)
                .energy(50)
                .happiness(50)
                .hunger(50)
                .build();

        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        PetResponse response = petService.createPet(request);

        assertEquals("Fluffy", response.getName());
        assertEquals("MOUNTAIN", response.getVariant());
        assertEquals("EGG", response.getStage());
    }

    @Test
    void getUserPets_ReturnsListOfPets() {
        Pet pet1 = Pet.builder()
                .name("Fluffy")
                .variant(Variant.MOUNTAIN)
                .owner(mockUser)
                .stage(Stage.EGG)
                .experience(0)
                .build();

        Pet pet2 = Pet.builder()
                .name("Spike")
                .variant(Variant.SWAMP)
                .owner(mockUser)
                .stage(Stage.EGG)
                .experience(0)
                .build();

        when(petRepository.findByOwner(mockUser)).thenReturn(List.of(pet1, pet2));

        List<PetResponse> pets = petService.getUserPets();

        assertEquals(2, pets.size());
        assertEquals("Fluffy", pets.get(0).getName());
    }

    @Test
    void getPetById_WithValidId_ReturnsPetResponse() {
        Pet pet = Pet.builder()
                .id(1L)
                .name("Fluffy")
                .variant(Variant.MOUNTAIN)
                .owner(mockUser)
                .stage(Stage.EGG)
                .experience(0)
                .build();
        when(petRepository.findByIdAndOwner(1L, mockUser)).thenReturn(Optional.of(pet));

        PetResponse response = petService.getPetById(1L);

        assertEquals("Fluffy", response.getName());
    }

    @Test
    void getPetById_WithInvalidId_ThrowsException() {
        when(petRepository.findByIdAndOwner(99L, mockUser)).thenReturn(Optional.empty());

        assertThrows(PetNotFoundException.class, () -> petService.getPetById(99L));
    }

    @Test
    void deletePet_WithValidId_DeletesPet() {
        Pet pet = Pet.builder().id(1L).name("Fluffy").owner(mockUser).build();
        when(petRepository.findByIdAndOwner(1L, mockUser)).thenReturn(Optional.of(pet));

        petService.deletePet(1L);

        verify(petRepository).delete(pet);
    }

    @Test
    void feedPet_UpdatesPetState() {
        Pet pet = Pet.builder()
                .id(1L)
                .name("Fluffy")
                .owner(mockUser)
                .variant(Variant.MOUNTAIN)
                .stage(Stage.EGG)
                .experience(0)
                .build();

        Pet updatedPet = Pet.builder()
                .id(1L)
                .name("Fluffy")
                .owner(mockUser)
                .variant(Variant.MOUNTAIN)
                .stage(Stage.EGG)
                .experience(0)
                .hunger(30)
                .build();

        when(petRepository.findByIdAndOwner(1L, mockUser)).thenReturn(Optional.of(pet));
        when(petActionService.applyAction(pet, mockUser, ActionType.FEED)).thenReturn(updatedPet);
        when(petRepository.save(updatedPet)).thenReturn(updatedPet);

        PetResponse response = petService.feedPet(1L);

        assertEquals("Fluffy", response.getName());
        assertEquals(30, response.getHunger());
    }
}