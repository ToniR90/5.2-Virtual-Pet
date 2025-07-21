package com.toni.virtualpet.model;

import com.toni.virtualpet.model.base.AuditableEntity;
import com.toni.virtualpet.model.enums.ActionType;
import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pet_action")
public class PetAction extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType actionType;

    @Column(name = "experience_gained")
    private Integer experienceGained = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id" , nullable = false)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        if (experienceGained == null) {
            experienceGained = actionType.getExperienceReward();
        }
        if (pet != null && user != null && !pet.getOwner().equals(user)) {
            throw new IllegalStateException("User must be the owner of the pet");
        }
    }

    @Override
    public String toString() {
        return "PetAction: " +
                "Id: " + id + "\n" +
                "Action Type: " + actionType + "\n" +
                "Experience Gained: " + experienceGained + "\n" +
                "Pet: " + pet + "\n" +
                "User: " + user;
    }
}
