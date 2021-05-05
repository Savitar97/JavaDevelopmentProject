package com.epam.training.ticketservice.core.booking.model;

import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.user.model.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class BookingDto {
    private final ScreeningDto screeningDto;
    private final UserDto userDto;
    private final List<SeatDto> seatDtos;
    private final Integer ticketPrice;

    @Override
    public String toString() {
        return "Seats "
                + seatDtos.stream()
                .map(Objects::toString)
                .collect(Collectors.joining(", "))
                + " on " + screeningDto.getMovie().getTitle()
                + " in room " + screeningDto.getRoom().getRoomName()
                + " starting at " + screeningDto.getStartTime()
                + " for " + ticketPrice + " HUF";
    }
}
