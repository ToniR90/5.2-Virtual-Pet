package com.toni.virtualpel.model;

import com.toni.virtualpel.model.enums.ActionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pet_action")
public class PetAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType actionType;

    @Column(name = "experience_gained")
    private int experienceGained = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id" , nullable = false)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;


    @Override
    public String toString() {
        return "Pet Action: " +
                "Id: " + id + "\n" +
                "Action Type: " + actionType + "\n" +
                "Experience Gained: " + experienceGained + "\n" +
                "User: " + user;
    }
}