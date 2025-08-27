package com.toni.virtualpet.controller;

import com.toni.virtualpet.dto.request.UpdateUserRequest;
import com.toni.virtualpet.dto.response.ApiResponse;
import com.toni.virtualpet.dto.response.UserResponse;
import com.toni.virtualpet.model.user.User;
import com.toni.virtualpet.service.user.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*" , maxAge = 3600)
@AllArgsConstructor
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        User user = userService.getCurrentUser();

        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully" , UserResponse.from(user)));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@Valid @RequestBody UpdateUserRequest request) {
        User updateUser = userService.updateUser(request);
        return ResponseEntity.ok(ApiResponse.success("User update successfully" , UserResponse.from(updateUser)));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<UserResponse>> deleteUserAccount() {
        userService.deleteCurrentUser();

        logger.info("User account deleted successfully");

        return ResponseEntity.ok(ApiResponse.success("User account deleted" , null));
    }
}
