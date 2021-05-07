package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.ui.utilities.out.helper.ConvertListToString;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;

@ShellComponent
public class MovieCommands extends CommandAvailability {
    private final MovieService movieService;
    private final ConvertListToString convertListToString;

    public MovieCommands(MovieService movieService, ConvertListToString convertListToString) {
        this.movieService = movieService;
        this.convertListToString = convertListToString;
    }

    @ShellMethod(value = "List the available movies", key = "list movies")
    public String listOfMovies() {
        List<MovieDto> movies = movieService.getMovieList();
        if (movies.isEmpty()) {
            return "There are no movies at the moment";
        }
        return convertListToString.listToString(movies);
    }

    @ShellMethodAvailability(value = "isUserAdmin")
    @ShellMethod(value = "Add a movie to movies", key = "create movie")
    public String createMovie(String title, String genre, Integer length) {
        MovieDto movieDto = new MovieDto.Builder()
                .withTitle(title)
                .withGenre(genre)
                .withLength(length)
                .build();
        try {
            movieService.createMovie(movieDto);
            return "Create was successful";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @ShellMethodAvailability(value = "isUserAdmin")
    @ShellMethod(value = "Delete a movie from movies", key = "delete movie")
    public String deleteMovieByTitle(String title) {
        try {
            movieService.deleteMovieByTitle(title);
            return "Delete was successful";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @ShellMethodAvailability(value = "isUserAdmin")
    @ShellMethod(value = "Update a movie from movies", key = "update movie")
    public String updateMovie(String title, String genre, Integer length) {
        MovieDto movieDto = new MovieDto.Builder()
                .withTitle(title)
                .withGenre(genre)
                .withLength(length)
                .build();
        try {
            movieService.updateMovie(movieDto);
            return "Update Success";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }
}
