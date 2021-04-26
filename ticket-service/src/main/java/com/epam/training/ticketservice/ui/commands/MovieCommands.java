package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ShellComponent
public class MovieCommands {
    private final MovieService movieService;

    public MovieCommands(MovieService movieService) {
        this.movieService = movieService;
    }

    @ShellMethod(value = "List the available movies", key = "list movies")
    public String listOfMovies() {
        List<MovieDto> movies = movieService.getMovieList();
        if (movies == null || movies.isEmpty()) {
            return "There are no movies at the moment";
        }
        return movies.stream().map(Objects::toString).collect(Collectors.joining());
    }

    @ShellMethod(value = "Add a movie to movies",key = "create movie")
    public MovieDto createMovie(String title,String genre,Integer length) {
        MovieDto movieDto = new MovieDto.Builder()
                .withTitle(title)
                .withGenre(genre)
                .withLength(length)
                .build();
        movieService.createMovie(movieDto);
        return movieDto;
    }

    @ShellMethod(value = "Delete a movie from movies", key = "delete movie")
    public void deleteMovieByTitle(String title) {
        movieService.deleteMovieByTitle(title);
    }

    @ShellMethod(value = "Update a movie from movies", key = "update movie")
    public void updateMovie(String title,String genre,Integer length) {
        MovieDto movieDto = new MovieDto.Builder()
                .withTitle(title)
                .withGenre(genre)
                .withLength(length)
                .build();
        movieService.updateMovie(movieDto);
    }
}
