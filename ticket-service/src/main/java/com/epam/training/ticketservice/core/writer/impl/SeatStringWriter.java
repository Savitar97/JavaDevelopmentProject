package com.epam.training.ticketservice.core.writer.impl;

import com.epam.training.ticketservice.core.booking.persistence.entity.Seat;
import com.epam.training.ticketservice.core.writer.OutputStringWriter;
import org.springframework.stereotype.Component;

@Component
public class SeatStringWriter implements OutputStringWriter<Seat> {

    @Override
    public String writeOutAsString(Seat object) {
        return "(" + object.getSeatRow()
                + "," + object.getSeatColumn()
                + ')';
    }
}
