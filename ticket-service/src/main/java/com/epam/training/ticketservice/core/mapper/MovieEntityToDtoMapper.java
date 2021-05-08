package com.epam.training.ticketservice.core.mapper;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;

import java.util.Optional;

public interface MovieEntityToDtoMapper {
    Optional<MovieDto> convertEntityToDto(Optional<Movie> movie);

    MovieDto convertEntityToDto(Movie movie);
}
