package com.epam.training.ticketservice.core.mapper;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;

import java.util.Optional;

public interface EntityToDtoMapper {

    Optional<MovieDto> convertEntityToDto(Optional<Movie> movie);

    MovieDto convertEntityToDto(Movie movie);

    RoomDto convertEntityToDto(Room room);

    ScreeningDto convertEntityToDto(Screening screening);
}
