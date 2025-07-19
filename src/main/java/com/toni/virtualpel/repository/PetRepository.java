package com.toni.virtualpel.repository;

import com.toni.virtualpel.model.Pet;
import com.toni.virtualpel.model.User;
import com.toni.virtualpel.model.enums.Stage;
import com.toni.virtualpel.model.enums.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByOwner (User owner);
    List<Pet> findByOwnerId(Long ownerId);
    List<Pet> findByIdAndOwner(Long id , User owner);

    @Query("SELECT p FROM Pet p WHERE p.owner.id = :ownerId")
    List<Pet> findPetsByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT COUNT(p) FROM Pet p WHERE p.owner.id = :ownerId")
    long countPetsByOwnerId(@Param("ownerId") Long ownerId);

    List<Pet> findByVariant(Variant variant);
    List<Pet> findByStage(Stage stage);

    @Query("SELECT p FROM Pet p WHERE p.variant = :variant AND p.stage = :stage")
    List<Pet> findByVariantAndStage(@Param("variant") Variant variant,
                                    @Param("stage") Stage stage);
}