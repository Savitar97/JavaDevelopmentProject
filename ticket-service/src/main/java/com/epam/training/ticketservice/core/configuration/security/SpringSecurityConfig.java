package com.epam.training.ticketservice.core.configuration.security;

import com.epam.training.ticketservice.core.user.impl.UserDetailsImpl;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.Collections;


@Configuration
public class SpringSecurityConfig {
    @Bean
    public SCryptPasswordEncoder passwordEncoder() {
        return new SCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new UserDetailsImpl(userRepository);
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider
                .setUserDetailsService(userDetailsService);
        authenticationProvider
                .setPasswordEncoder(passwordEncoder);

        return new ProviderManager(Collections.singletonList(authenticationProvider));
    }
}
