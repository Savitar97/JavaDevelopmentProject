package com.epam.training.ticketservice.core.writer.impl;

import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.writer.OutputStringWriter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BookingStringWriter implements OutputStringWriter<Booking> {
    final SeatStringWriter seatStringWriter;

    public BookingStringWriter(SeatStringWriter seatStringWriter) {
        this.seatStringWriter = seatStringWriter;
    }

    @Override
    public String writeOutAsString(Booking object) {
        return "Seats booked: "
                + object.getSeats().stream()
                .map(seatStringWriter::writeOutAsString)
                .collect(Collectors.joining(", "))
                + "; the price for this booking is "
                + object.getTicketPrice()
                + " HUF";
    }
}
