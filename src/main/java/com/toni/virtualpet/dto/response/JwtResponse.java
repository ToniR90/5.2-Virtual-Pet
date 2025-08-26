package com.toni.virtualpet.dto.response;

import com.toni.virtualpet.model.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private Role role;

    public JwtResponse(String token, Long id, String username, Role role) {
        this.token = token;
        this.type = "Bearer";
        this.id = id;
        this.username = username;
        this.role = role;
    }

}
