package com.epam.training.ticketservice.core.booking.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Seat {
    private Integer seatRow;
    private Integer seatColumn;

    @Override
    public String toString() {
        return "(" + seatRow
                + ","+ seatColumn
                + ')';
    }
}
