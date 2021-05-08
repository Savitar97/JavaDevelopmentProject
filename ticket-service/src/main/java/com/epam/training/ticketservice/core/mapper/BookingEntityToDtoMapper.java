package com.epam.training.ticketservice.core.mapper;

import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;

public interface BookingEntityToDtoMapper {
    BookingDto convertEntityToDto(Booking booking);
}
