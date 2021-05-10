package com.epam.training.ticketservice.core.configuration;

import com.epam.training.ticketservice.core.pricecomponent.persistence.entity.BasePrice;
import com.epam.training.ticketservice.core.pricecomponent.persistence.repository.BasePriceRepository;
import com.epam.training.ticketservice.core.user.persistence.entity.Role;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DbInitializer {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final BasePriceRepository basePriceRepository;

    public DbInitializer(PasswordEncoder passwordEncoder,
                         UserRepository userRepository,
                         BasePriceRepository basePriceRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.basePriceRepository = basePriceRepository;
    }

    @PostConstruct
    public void init() {
        if (!userRepository.existsByUsername("admin")) {
            User user = new User(null, "admin",
                    passwordEncoder.encode("admin"),
                    Role.ROLE_ADMIN);
            userRepository.save(user);
        }
        if (!basePriceRepository.existsById(1)) {
            BasePrice basePrice = new BasePrice(1,1500);
            basePriceRepository.save(basePrice);
        }
    }
}
