package com.epam.training.ticketservice.core.pricecomponent.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasePrice {
    @Id
    private Integer id;

    private Integer price;

}
