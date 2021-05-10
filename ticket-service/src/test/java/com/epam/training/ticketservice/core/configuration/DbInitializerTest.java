package com.epam.training.ticketservice.core.configuration;

import com.epam.training.ticketservice.core.pricecomponent.persistence.entity.BasePrice;
import com.epam.training.ticketservice.core.pricecomponent.persistence.repository.BasePriceRepository;
import com.epam.training.ticketservice.core.user.persistence.entity.Role;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class DbInitializerTest {
    private UserRepository userRepository;
    private DbInitializer underTest;
    private BasePriceRepository basePriceRepository;
    private final static PasswordEncoder passwordEncoder
            = NoOpPasswordEncoder.getInstance();

    private User user = new User(null, "admin",
            passwordEncoder.encode("admin"),
            Role.ROLE_ADMIN);

    @BeforeEach
    public void init() {
        userRepository = Mockito.mock(UserRepository.class);
        basePriceRepository = Mockito.mock(BasePriceRepository.class);
        underTest = new DbInitializer(passwordEncoder, userRepository, basePriceRepository);
    }

    @Test
    public void initShouldCallTheUserRepositoryAndSaveAdminUserWhenAdminNotExist() {
        //Given
        Mockito.when(userRepository
                .existsByUsername("admin"))
                .thenReturn(false);
        Mockito.when(basePriceRepository.existsById(1))
                .thenReturn(false);
        Mockito.when(userRepository
                .save(user))
                .thenReturn(user);
        //When
        underTest.init();
        //Then
        Mockito.verify(basePriceRepository).save(new BasePrice(1,1500));
        Mockito.verify(basePriceRepository).existsById(1);
        Mockito.verify(userRepository)
                .save(user);
        Mockito.verify(userRepository)
                .existsByUsername("admin");
        Mockito.verifyNoMoreInteractions(userRepository);
        Mockito.verifyNoMoreInteractions(basePriceRepository);
    }

    @Test
    public void initShouldCallTheUserRepositoryAndSaveAdminUserWhenAdminExist() {
        //Given
        Mockito.when(userRepository
                .existsByUsername("admin"))
                .thenReturn(true);
        Mockito.when(basePriceRepository.existsById(1))
                .thenReturn(true);
        //When
        underTest.init();
        //Then
        Mockito.verify(userRepository)
                .existsByUsername("admin");
        Mockito.verify(basePriceRepository)
                .existsById(1);
        Mockito.verifyNoMoreInteractions(userRepository);
        Mockito.verifyNoMoreInteractions(basePriceRepository);
    }


}