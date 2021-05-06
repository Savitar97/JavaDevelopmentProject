package com.epam.training.ticketservice.core.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class SeatDto {
    private final Integer row;
    private final Integer column;

    @Override
    public String toString() {
        return "(" + row + ',' + column + ')';
    }
}
