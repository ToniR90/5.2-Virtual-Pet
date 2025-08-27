package com.toni.virtualpet.controller;

import com.toni.virtualpet.dto.response.ApiResponse;
import com.toni.virtualpet.dto.response.UserResponse;
import com.toni.virtualpet.model.user.User;
import com.toni.virtualpet.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*" , maxAge = 3600)
@AllArgsConstructor
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        User user = userService.getCurrentUser();

        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully" , UserResponse.from(user)));
    }
}
