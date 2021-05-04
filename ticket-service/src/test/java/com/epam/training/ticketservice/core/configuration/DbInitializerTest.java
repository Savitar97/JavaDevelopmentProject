package com.epam.training.ticketservice.core.configuration;

import com.epam.training.ticketservice.core.user.persistence.entity.Role;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

class DbInitializerTest {
    private UserRepository userRepository;
    private DbInitializer underTest;
    private final static PasswordEncoder passwordEncoder
            = NoOpPasswordEncoder.getInstance();

    private User user = new User(null,"admin",
            passwordEncoder.encode("admin"),
            Role.ROLE_ADMIN);

    @BeforeEach
    public void init() {
        userRepository = Mockito.mock(UserRepository.class);
        underTest = new DbInitializer(passwordEncoder,userRepository);
    }

    @Test
    public void initShouldCallTheUserRepositoryAndSaveAdminUserWhenAdminNotExist(){
        //Given
        Mockito.when(userRepository.existsByUsername("admin")).thenReturn(false);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        //When
        underTest.init();
        //Then
        Mockito.verify(userRepository).save(user);
        Mockito.verify(userRepository).existsByUsername("admin");
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void initShouldCallTheUserRepositoryAndSaveAdminUserWhenAdminExist(){
        //Given
        Mockito.when(userRepository.existsByUsername("admin")).thenReturn(true);
        //When
        underTest.init();
        //Then
        Mockito.verify(userRepository).existsByUsername("admin");
        Mockito.verifyNoMoreInteractions(userRepository);
    }


}