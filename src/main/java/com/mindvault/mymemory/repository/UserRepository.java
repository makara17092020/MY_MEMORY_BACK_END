package com.mindvault.mymemory.repository;

import com.mindvault.mymemory.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // Method used by UserService to check for existing users and load UserDetails
    Optional<User> findByUsername(String username);

    // Find by email to prevent duplicate email registration
    Optional<User> findByEmail(String email);
}