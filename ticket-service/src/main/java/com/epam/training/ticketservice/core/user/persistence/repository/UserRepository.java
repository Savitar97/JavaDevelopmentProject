package com.epam.training.ticketservice.core.user.persistence.repository;

import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);
}
