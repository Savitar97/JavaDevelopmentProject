package com.epam.training.ticketservice.core.mapper.impl;

import com.epam.training.ticketservice.core.booking.model.SeatDto;
import com.epam.training.ticketservice.core.booking.persistence.entity.Seat;
import com.epam.training.ticketservice.core.mapper.SeatEntityToDtoMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SeatEntityToDtoMapperImpl implements SeatEntityToDtoMapper {
    @Override
    public List<SeatDto> convertEntityToDto(List<Seat> seats) {
        return seats.stream()
                .map(seat -> SeatDto.builder()
                        .row(seat.getSeatRow())
                        .column(seat.getSeatColumn())
                        .build())
                .collect(Collectors.toList());
    }
}
