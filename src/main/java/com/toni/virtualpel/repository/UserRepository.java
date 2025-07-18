package com.toni.virtualpel.repository;

import com.toni.virtualpel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
