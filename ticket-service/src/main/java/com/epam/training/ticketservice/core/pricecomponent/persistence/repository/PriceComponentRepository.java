package com.epam.training.ticketservice.core.pricecomponent.persistence.repository;

import com.epam.training.ticketservice.core.pricecomponent.persistence.entity.PriceComponent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriceComponentRepository extends JpaRepository<PriceComponent, Long> {
    Optional<PriceComponent> findPriceComponentByName(String name);

    boolean existsPriceComponentByName(String name);
}
