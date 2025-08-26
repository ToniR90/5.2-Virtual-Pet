package com.toni.virtualpet.service.user;

import com.toni.virtualpet.exception.personalizedException.UserNotFoundException;
import com.toni.virtualpet.model.user.User;
import com.toni.virtualpet.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
