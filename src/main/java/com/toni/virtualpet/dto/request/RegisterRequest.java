package com.toni.virtualpet.dto.request;


import com.toni.virtualpet.model.user.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterRequest {
    @NotBlank(message = "Email can't be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Name can't be empty")
    @Size(min = 3 , max = 20 , message = "Name must be between 3 - 20 chars long")
    private String username;

    @NotBlank(message = "Password can't be empty")
    @Size(min = 6 , message = "Min size: 6 chars")
    private String password;

    private Role role = Role.ROLE_USER;
}