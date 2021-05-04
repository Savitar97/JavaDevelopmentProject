package com.epam.training.ticketservice.core.user.impl;

import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.RegistrationUserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.Role;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(RegistrationUserDto registrationUser) {
        Objects.requireNonNull(registrationUser.getUsername(), "Username can not be null");
        Objects.requireNonNull(registrationUser.getPassword(), "Password can not be null");
        if (!userRepository.existsByUsername(registrationUser.getUsername())) {
            User user = new User();
            user.setUsername(registrationUser.getUsername());
            user.setPassword(passwordEncoder.encode(registrationUser.getPassword()));
            user.setRole(Role.ROLE_USER);

            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User with this name already exist");
        }
    }

    @Override
    public String describeAccount() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Optional<? extends GrantedAuthority> role = authentication.getAuthorities().stream().findFirst();
            if (role.get().equals(Role.ROLE_ADMIN)) {
                return "Signed in with privileged account "
                        + SecurityContextHolder.getContext().getAuthentication().getName();
            } else {
                return "Signed in with account "
                        + SecurityContextHolder.getContext().getAuthentication().getName();
            }
        } catch (Exception e) {
            return "You are not signed in";
        }

    }
}
