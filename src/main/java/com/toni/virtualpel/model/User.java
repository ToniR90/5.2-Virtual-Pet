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
    @Column(name = "user_name", nullable = false)
    private String userName;

    @NotBlank
    @Email(message = "Invalid email format")
    @Column(name = "email", nullable = false)
    private String email;

    @NotBlank
    @Column(name = "password", nullable = false, unique = true)
    @Size(min = 6 , message = "Password minimum length is 6 chars")
    private String password;

    @NotNull(message = "User role must be specified")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.PLAYER;

    @OneToMany(mappedBy = "owner" , cascade = CascadeType.ALL , orphanRemoval = true)
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