package com.toni.virtualpet.dto.response;

import com.toni.virtualpet.model.enums.Role;
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
}
