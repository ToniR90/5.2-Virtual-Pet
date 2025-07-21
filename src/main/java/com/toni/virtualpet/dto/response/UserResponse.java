package com.toni.virtualpet.dto.response;

import com.toni.virtualpet.model.User;
import com.toni.virtualpet.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponse {
    private Long id;
    private String email;
    private String username;
    private Role role;

    public static UserResponse from(User user) {
        UserResponse response = new UserResponse();
        response.id = user.getId();
        response.email = user.getEmail();
        response.username = user.getUsername();
        response.role = user.getRole();
        return response;
    }
}