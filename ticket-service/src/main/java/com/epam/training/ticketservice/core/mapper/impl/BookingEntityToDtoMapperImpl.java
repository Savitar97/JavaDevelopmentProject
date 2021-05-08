package com.epam.training.ticketservice.core.mapper.impl;

import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.mapper.BookingEntityToDtoMapper;
import com.epam.training.ticketservice.core.mapper.ScreeningEntityToDtoMapper;
import com.epam.training.ticketservice.core.mapper.SeatEntityToDtoMapper;
import com.epam.training.ticketservice.core.mapper.UserEntityToDtoMapper;
import org.springframework.stereotype.Component;

@Component
public class BookingEntityToDtoMapperImpl implements BookingEntityToDtoMapper {
    private final ScreeningEntityToDtoMapper screeningEntityToDtoMapper;
    private final SeatEntityToDtoMapper seatEntityToDtoMapper;
    private final UserEntityToDtoMapper userEntityToDtoMapper;

    public BookingEntityToDtoMapperImpl(ScreeningEntityToDtoMapper screeningEntityToDtoMapper,
                                        SeatEntityToDtoMapper seatEntityToDtoMapper,
                                        UserEntityToDtoMapper userEntityToDtoMapper) {
        this.screeningEntityToDtoMapper = screeningEntityToDtoMapper;
        this.seatEntityToDtoMapper = seatEntityToDtoMapper;
        this.userEntityToDtoMapper = userEntityToDtoMapper;
    }

    @Override
    public BookingDto convertEntityToDto(Booking booking) {
        return BookingDto.builder()
                .screeningDto(screeningEntityToDtoMapper
                        .convertEntityToDto(booking.getScreening()))
                .seatDtos(seatEntityToDtoMapper
                        .convertEntityToDto(booking.getSeats()))
                .userDto(userEntityToDtoMapper
                        .convertEntityToDto(booking.getUser()))
                .ticketPrice(booking.getTicketPrice()).build();
    }
}
