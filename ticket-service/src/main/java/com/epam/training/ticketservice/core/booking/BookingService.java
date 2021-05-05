package com.epam.training.ticketservice.core.booking;

import com.epam.training.ticketservice.core.booking.model.SeatDto;

import java.util.Date;
import java.util.List;

public interface BookingService {
    public String createBooking(String movieTitle, String roomName, Date startTime, List<SeatDto> seats);
}
