package com.toni.virtualpet.repository;

import com.toni.virtualpet.model.pet.Pet;
import com.toni.virtualpet.model.petAction.PetAction;
import com.toni.virtualpet.model.user.User;
import com.toni.virtualpet.model.petAction.enums.ActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PetActionRepository extends JpaRepository<PetAction , Long> {
    List<PetAction> findByPet(Pet pet);
    List<PetAction> findByPetId(Long petId);
    List<PetAction> findByUser(User user);
    List<PetAction> findByPetOrderByCreatedAtDesc(Pet pet);
    List<PetAction> findByActionType(ActionType actionType);

    @Query("SELECT pa FROM PetAction pa WHERE pa.pet.id = :petId AND pa.createdAt >= :since ORDER BY pa.createdAt DESC")
    List<PetAction> findRecentActionsByPet(@Param("petId") Long petId, @Param("since") LocalDateTime since);

    @Query("SELECT COUNT(pa) FROM PetAction pa WHERE pa.pet.id = :petId AND pa.actionType = :actionType")
    long countActionsByPetAndType(@Param("petId") Long petId, @Param("actionType") ActionType actionType);

    @Query("SELECT pa FROM PetAction pa WHERE pa.user.id = :userId ORDER BY pa.createdAt DESC")
    List<PetAction> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
}
