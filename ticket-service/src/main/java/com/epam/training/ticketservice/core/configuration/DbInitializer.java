package com.epam.training.ticketservice.core.configuration;

import com.epam.training.ticketservice.core.user.persistence.entity.Role;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Component
public class DbInitializer {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public DbInitializer(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        User user = new User(null,"admin",
                passwordEncoder.encode("admin"),
                Role.ROLE_ADMIN);
        userRepository.save(user);
    }
}
