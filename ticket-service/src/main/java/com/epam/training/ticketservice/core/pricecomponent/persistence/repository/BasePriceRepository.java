package com.epam.training.ticketservice.core.pricecomponent.persistence.repository;

import com.epam.training.ticketservice.core.pricecomponent.persistence.entity.BasePrice;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;

public interface BasePriceRepository extends JpaRepository<BasePrice,Integer> {
    BasePrice getBasePriceById(Integer id);

    boolean existsById(Integer id);
}
