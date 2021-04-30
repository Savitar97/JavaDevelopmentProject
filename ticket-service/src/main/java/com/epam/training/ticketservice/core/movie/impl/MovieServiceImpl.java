package com.epam.training.ticketservice.core.movie.impl;

import com.epam.training.ticketservice.core.mapper.EntityToDtoMapper;
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
    private final EntityToDtoMapper entityToDtoMapper;

    MovieServiceImpl(MovieRepository movieRepository, EntityToDtoMapper entityToDtoMapper) {
        this.movieRepository = movieRepository;
        this.entityToDtoMapper = entityToDtoMapper;
    }

    @Override
    public List<MovieDto> getMovieList() {
        return movieRepository.findAll().stream()
                .map(entityToDtoMapper::convertEntityToDto)
                .collect(Collectors.toList());
    }


    @Override
    public void createMovie(MovieDto movieDto) {
        Objects.requireNonNull(movieDto, "Movie cannot be null");
        Objects.requireNonNull(movieDto.getTitle(), "Movie Title cannot be null");
        Objects.requireNonNull(movieDto.getLength(), "Movie Length cannot be null");
        Objects.requireNonNull(movieDto.getGenre(), "Movie Genre cannot be null");
        if (movieRepository.existsByTitle(movieDto.getTitle())) {
            throw new IllegalArgumentException("Movie with this title already exist");
        } else {
            Movie movie = new Movie(null,
                    movieDto.getTitle(),
                    movieDto.getGenre(),
                    movieDto.getLength()
            );
            movieRepository.save(movie);
        }
    }

    public void deleteMovieByTitle(String title) {
        if (movieRepository.existsByTitle(title)) {
            movieRepository.deleteByTitle(title);
        } else {
            throw new IllegalArgumentException("Movie with this title doesn't exist!");
        }
    }

    @Override
    public void updateMovie(MovieDto movieDto) {
        if (movieRepository.existsByTitle(movieDto.getTitle())) {
            Movie movie = movieRepository.getMovieByTitle(movieDto.getTitle());
            movie.setGenre(movieDto.getGenre());
            movie.setLength(movieDto.getLength());
            movieRepository.save(movie);
        } else {
            throw new IllegalArgumentException("Movie with this title doesn't exist!");
        }
    }

    @Override
    public Optional<MovieDto> findByTitle(String title) {
        return entityToDtoMapper.convertEntityToDto(movieRepository.findByTitle(title));
    }

    @Override
    public Boolean existsByTitle(String title) {
        return movieRepository.existsByTitle(title);
    }


}
