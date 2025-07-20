package com.toni.virtualpel.service;

import com.toni.virtualpel.dto.CreatePetRequest;
import com.toni.virtualpel.dto.PetResponse;
import com.toni.virtualpel.model.Pet;
import com.toni.virtualpel.model.User;
import com.toni.virtualpel.repository.PetActionRepository;
import com.toni.virtualpel.repository.PetRepository;
import com.toni.virtualpel.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Transactional
public class PetService {

    private static final Logger logger = LoggerFactory.getLogger(PetService.class);

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetActionRepository petActionRepository;


    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}