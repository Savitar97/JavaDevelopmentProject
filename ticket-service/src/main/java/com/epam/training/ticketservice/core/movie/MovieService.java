package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.pricecomponent.persistence.entity.PriceComponent;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<MovieDto> getMovieList();

    void createMovie(MovieDto movieDto);

    void deleteMovieByTitle(String title);

    void updateMovie(MovieDto movieDto);

    Optional<MovieDto> findByTitle(String title);

    Boolean existsByTitle(String title);

    void updatePriceComponent(PriceComponent priceComponent, String movieTitle);

}
