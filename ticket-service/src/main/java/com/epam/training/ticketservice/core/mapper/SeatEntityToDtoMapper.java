package com.epam.training.ticketservice.core.mapper;

import com.epam.training.ticketservice.core.booking.model.SeatDto;
import com.epam.training.ticketservice.core.booking.persistence.entity.Seat;

import java.util.List;

public interface SeatEntityToDtoMapper {
    List<SeatDto> convertEntityToDto(List<Seat> seat);
}
