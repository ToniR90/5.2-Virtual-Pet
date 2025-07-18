package com.toni.virtualpel.repository;

import com.toni.virtualpel.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}