package com.epam.training.ticketservice.core.user.impl;

import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.RegistrationUserDto;
import com.epam.training.ticketservice.core.user.model.UserDto;
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
        Objects.requireNonNull(registrationUser, "Registration user can not be null");
        Objects.requireNonNull(registrationUser.getUsername(), "Username can not be null");
        Objects.requireNonNull(registrationUser.getPassword(), "Password can not be null");

        if (userRepository.existsByUsername(registrationUser.getUsername())) {
            throw new IllegalArgumentException("User with this name already exist");
        }
        
        User user = new User();
        user.setUsername(registrationUser.getUsername());
        user.setPassword(passwordEncoder.encode(registrationUser.getPassword()));
        user.setRole(Role.ROLE_USER);

        userRepository.save(user);
    }

    @Override
    public UserDto describeAccount() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Optional<? extends GrantedAuthority> role = authentication.getAuthorities().stream().findFirst();
            return UserDto.builder()
                    .username(SecurityContextHolder.getContext().getAuthentication().getName())
                    .role((Role) role.get())
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException("You are not signed in");
        }
    }
}
