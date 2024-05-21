package com.devb.estores.repository;

import com.devb.estores.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
