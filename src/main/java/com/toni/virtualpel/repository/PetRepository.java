package com.toni.virtualpel.repository;

import com.toni.virtualpel.model.Pet;
import com.toni.virtualpel.model.User;
import com.toni.virtualpel.model.enums.Stage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByOwner (User owner);
    List<Pet> findByStage (Stage stage);
}