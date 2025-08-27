package com.toni.virtualpet.service.user;

import com.toni.virtualpet.dto.request.UpdateUserRequest;
import com.toni.virtualpet.exception.personalizedException.UserAlreadyExistsException;
import com.toni.virtualpet.exception.personalizedException.UserNotFoundException;
import com.toni.virtualpet.model.user.User;
import com.toni.virtualpet.model.user.enums.Role;
import com.toni.virtualpet.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User updateUser(UpdateUserRequest request) {
        User currentUser = getCurrentUser();

        if (!currentUser.getUsername().equals(request.getUsername()) &&
                userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already in use");
        }

        if (!currentUser.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already in use");
        }

        currentUser.setUsername(request.getUsername());
        currentUser.setEmail(request.getEmail());

        return userRepository.save(currentUser);
    }

    public List<User> getUserByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public void deleteCurrentUser() {
        User currentUser = getCurrentUser();
        userRepository.delete(currentUser);
    }
}
