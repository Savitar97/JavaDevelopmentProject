package com.epam.training.ticketservice.core.movie.impl;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<MovieDto> getMovieList() {
        return movieRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }


    @Override
    public void createMovie(MovieDto movieDto) {
        Objects.requireNonNull(movieDto, "Movie cannot be null");
        Objects.requireNonNull(movieDto.getTitle(), "Movie Title cannot be null");
        Objects.requireNonNull(movieDto.getLength(), "Movie Length cannot be null");
        Movie movie = new Movie(null,
                movieDto.getTitle(),
                movieDto.getGenre(),
                movieDto.getLength()
                );
        movieRepository.save(movie);
    }

    public void deleteMovieByTitle(String title) {
        movieRepository.deleteByTitle(title);
    }

    @Override
    public void updateMovie(MovieDto movieDto) {
        Movie movie = movieRepository.findByTitle(movieDto.getTitle());
        movie.setGenre(movieDto.getGenre());
        movie.setLength(movieDto.getLength());
        movieRepository.save(movie);
    }


    private MovieDto convertEntityToDto(Movie movie) {
        return new MovieDto.Builder()
                .withTitle(movie.getTitle())
                .withLength(movie.getLength())
                .withGenre(movie.getGenre())
                .build();
    }

    private Optional<MovieDto> convertEntityToDto(Optional<Movie> movie) {
        Optional<MovieDto> movieDto;
        if (movie.isEmpty()) {
            movieDto = Optional.empty();
        } else {
            movieDto = Optional.of(convertEntityToDto(movie.get()));
        }
        return movieDto;
    }

}
