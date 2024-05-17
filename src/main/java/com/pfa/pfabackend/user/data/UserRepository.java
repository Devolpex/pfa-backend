package com.pfa.pfabackend.user.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfa.pfabackend.user.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
    
}
