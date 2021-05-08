package com.epam.training.ticketservice.core.mapper.impl;

import com.epam.training.ticketservice.core.mapper.MovieEntityToDtoMapper;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MovieEntityToDtoMapperImpl implements MovieEntityToDtoMapper {
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
}
