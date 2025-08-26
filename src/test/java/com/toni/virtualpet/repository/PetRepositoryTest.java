package com.toni.virtualpet.repository;

import com.toni.virtualpet.model.pet.Pet;
import com.toni.virtualpet.model.user.User;
import com.toni.virtualpet.model.user.enums.Role;
import com.toni.virtualpet.model.pet.enums.Stage;
import com.toni.virtualpet.model.pet.enums.Variant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PetRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Pet mountainDragon;
    private Pet swampDragon;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("hashedPassword")
                .role(Role.ROLE_USER)
                .build();
        testUser = entityManager.persistAndFlush(testUser);

        mountainDragon = Pet.builder()
                .name("Draco")
                .variant(Variant.MOUNTAIN)
                .stage(Stage.YOUNG)
                .experience(4)
                .energy(75)
                .happiness(80)
                .hunger(60)
                .owner(testUser)
                .build();

        swampDragon = Pet.builder()
                .name("Shadow")
                .variant(Variant.SWAMP)
                .stage(Stage.ADULT)
                .experience(7)
                .energy(85)
                .happiness(70)
                .hunger(50)
                .owner(testUser)
                .build();

        entityManager.persistAndFlush(mountainDragon);
        entityManager.persistAndFlush(swampDragon);
    }

    @Test
    void findByOwner_ShouldReturnUserPets() {
        List<Pet> pets = petRepository.findByOwner(testUser);

        assertThat(pets).hasSize(2);
        assertThat(pets).extracting(Pet::getName).contains("Draco", "Shadow");
    }

    @Test
    void findByOwnerId_ShouldReturnUserPets() {
        List<Pet> pets = petRepository.findByOwnerId(testUser.getId());

        assertThat(pets).hasSize(2);
        assertThat(pets.get(0).getOwner().getId()).isEqualTo(testUser.getId());
    }

    @Test
    void findByIdAndOwner_ShouldReturnPetIfOwnerMatches() {
        List<Pet> pets = petRepository.findByIdAndOwner(mountainDragon.getId(), testUser);

        assertThat(pets).hasSize(1);
        assertThat(pets.get(0).getName()).isEqualTo("Draco");
        assertThat(pets.get(0).getOwner()).isEqualTo(testUser);
    }

    @Test
    void findByIdAndOwner_ShouldReturnEmptyIfOwnerDoesNotMatch() {
        User anotherUser = User.builder()
                .username("anotheruser")
                .email("another@example.com")
                .password("hashedPassword")
                .role(Role.ROLE_USER)
                .build();
        anotherUser = entityManager.persistAndFlush(anotherUser);

        List<Pet> pets = petRepository.findByIdAndOwner(mountainDragon.getId(), anotherUser);

        assertThat(pets).isEmpty();
    }

    @Test
    void countPetsByOwnerId_ShouldReturnCorrectCount() {
        long count = petRepository.countPetsByOwnerId(testUser.getId());

        assertThat(count).isEqualTo(2);
    }

    @Test
    void findByVariant_ShouldReturnPetsOfSpecificVariant() {
        List<Pet> mountainPets = petRepository.findByVariant(Variant.MOUNTAIN);
        List<Pet> swampPets = petRepository.findByVariant(Variant.SWAMP);

        assertThat(mountainPets).hasSize(1);
        assertThat(mountainPets.get(0).getName()).isEqualTo("Draco");

        assertThat(swampPets).hasSize(1);
        assertThat(swampPets.get(0).getName()).isEqualTo("Shadow");
    }

    @Test
    void findByStage_ShouldReturnPetsOfSpecificStage() {
        List<Pet> youngPets = petRepository.findByStage(Stage.YOUNG);
        List<Pet> adultPets = petRepository.findByStage(Stage.ADULT);

        assertThat(youngPets).hasSize(1);
        assertThat(youngPets.get(0).getName()).isEqualTo("Draco");

        assertThat(adultPets).hasSize(1);
        assertThat(adultPets.get(0).getName()).isEqualTo("Shadow");
    }

    @Test
    void findByVariantAndStage_ShouldReturnPetsMatchingBothCriteria() {
        List<Pet> mountainYoungPets = petRepository.findByVariantAndStage(Variant.MOUNTAIN, Stage.YOUNG);
        List<Pet> swampYoungPets = petRepository.findByVariantAndStage(Variant.SWAMP, Stage.YOUNG);

        assertThat(mountainYoungPets).hasSize(1);
        assertThat(mountainYoungPets.get(0).getName()).isEqualTo("Draco");

        assertThat(swampYoungPets).isEmpty();
    }

    @Test
    void save_ShouldPersistPetCorrectly() {
        Pet newPet = Pet.builder()
                .name("Leafy")
                .variant(Variant.FOREST)
                .stage(Stage.EGG)
                .experience(0)
                .energy(50)
                .happiness(50)
                .hunger(50)
                .owner(testUser)
                .build();

        Pet savedPet = petRepository.save(newPet);

        assertThat(savedPet.getId()).isNotNull();
        assertThat(savedPet.getName()).isEqualTo("Leafy");
        assertThat(savedPet.getVariant()).isEqualTo(Variant.FOREST);

        Pet foundPet = entityManager.find(Pet.class, savedPet.getId());
        assertThat(foundPet).isNotNull();
        assertThat(foundPet.getName()).isEqualTo("Leafy");
    }

    @Test
    void delete_ShouldRemovePetFromDatabase() {
        Long petId = mountainDragon.getId();

        petRepository.delete(mountainDragon);
        entityManager.flush();

        Pet foundPet = entityManager.find(Pet.class, petId);
        assertThat(foundPet).isNull();

        long count = petRepository.countPetsByOwnerId(testUser.getId());
        assertThat(count).isEqualTo(1);
    }
}