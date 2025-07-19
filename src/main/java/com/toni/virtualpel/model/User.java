package com.toni.virtualpel.model;

import com.toni.virtualpel.model.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3 , max = 20 , message = "The username must have 3 - 20 chars")
    @Column(name = "user_name" , unique = true , nullable = false)
    private String userName;

    @NotBlank
    @Email(message = "Invalid email format")
    @Column(name = "email", nullable = false)
    private String email;

    @NotBlank
    @Column(name = "password", nullable = false)
    @Size(min = 6 , message = "Password minimum length is 6 chars")
    private String password;

    @NotNull(message = "User role must be specified")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.ROLE_USER;

    @Column(name = "created_at" , nullable = false)
    private LocalTime createdAt;

    @Column(name = "updated_at")
    private LocalTime updatedAt;

    @OneToMany(mappedBy = "owner" , cascade = CascadeType.ALL , orphanRemoval = true , fetch = FetchType.LAZY)
    private List<Pet> pets;

    @Override
    public String toString() {
        return "User: " + "\n" +
                "Id: " + id + "\n" +
                "User Name: " + userName + "\n" +
                "Email: " + email + "\n" +
                "Password: " + password + "\n" +
                "Role: " + role + "\n" +
                "Pets: " + pets;
    }
}