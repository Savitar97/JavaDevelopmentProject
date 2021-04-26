package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.model.MovieDto;

import java.util.List;

public interface MovieService {
    List<MovieDto> getMovieList();

    void createMovie(MovieDto movieDto);

    void deleteMovieByTitle(String title);

    void updateMovie(MovieDto movieDto);

}
