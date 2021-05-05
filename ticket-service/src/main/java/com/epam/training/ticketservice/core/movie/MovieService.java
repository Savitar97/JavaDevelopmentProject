package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.model.MovieDto;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<MovieDto> getMovieList();

    String createMovie(MovieDto movieDto);

    void deleteMovieByTitle(String title);

    void updateMovie(MovieDto movieDto);

    Optional<MovieDto> findByTitle(String title);

    Boolean existsByTitle(String title);

}
