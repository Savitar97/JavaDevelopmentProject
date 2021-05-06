package com.epam.training.ticketservice.core.mapper.impl;


import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.model.SeatDto;
import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.entity.Seat;
import com.epam.training.ticketservice.core.mapper.EntityToDtoMapper;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.user.model.RegistrationUserDto;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EntityToDtoMapperImpl implements EntityToDtoMapper {

    public MovieDto convertEntityToDto(Movie movie) {
        return new MovieDto.Builder()
                .withTitle(movie.getTitle())
                .withLength(movie.getLength())
                .withGenre(movie.getGenre())
                .build();
    }

    public Optional<MovieDto> convertEntityToDto(Optional<Movie> movie) {
        Optional<MovieDto> movieDto;
        if (movie.isEmpty()) {
            movieDto = Optional.empty();
        } else {
            movieDto = Optional.of(convertEntityToDto(movie.get()));
        }
        return movieDto;
    }

    public RoomDto convertEntityToDto(Room room) {
        return new RoomDto.Builder()
                .withRoomName(room.getName())
                .withSeatRows(room.getSeatRows())
                .withSeatColumns(room.getSeatColumns())
                .build();
    }

    public ScreeningDto convertEntityToDto(Screening screening) {
        return ScreeningDto.builder()
                .movie(convertEntityToDto(screening.getId().getMovie()))
                .room(convertEntityToDto(screening.getId().getRoom()))
                .startTime(screening.getId().getStartTime())
                .build();
    }

    @Override
    public UserDto convertEntityToDto(User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    @Override
    public List<SeatDto> convertEntityToDto(List<Seat> seats) {
        return seats.stream()
                .map(seat -> SeatDto.builder()
                        .row(seat.getSeatRow())
                        .column(seat.getSeatColumn())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public BookingDto convertEntityToDto(Booking booking) {
        return BookingDto.builder()
                .screeningDto(convertEntityToDto(booking.getScreening()))
                .seatDtos(convertEntityToDto(booking.getSeats()))
                .userDto(convertEntityToDto(booking.getUser()))
                .ticketPrice(booking.getTicketPrice()).build();
    }


}
