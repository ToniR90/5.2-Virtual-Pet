package com.toni.virtualpel.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String username;
    private String role;
}
