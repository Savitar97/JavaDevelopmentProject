package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.ui.utilities.out.helper.ConvertListToString;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

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
        if (movies == null || movies.isEmpty()) {
            return "There are no movies at the moment";
        }
        return convertListToString.listToString(movies);
    }

    @ShellMethod(value = "Add a movie to movies", key = "create movie")
    public String createMovie(String title, String genre, Integer length) {
        if (movieService.existsByTitle(title)) {
            return "Movie with this name already exist.";
        }
        MovieDto movieDto = new MovieDto.Builder()
                .withTitle(title)
                .withGenre(genre)
                .withLength(length)
                .build();
        movieService.createMovie(movieDto);
        return movieDto.toString();
    }

    @ShellMethod(value = "Delete a movie from movies", key = "delete movie")
    public void deleteMovieByTitle(String title) {
        movieService.deleteMovieByTitle(title);
    }

    @ShellMethod(value = "Update a movie from movies", key = "update movie")
    public void updateMovie(String title, String genre, Integer length) {
        MovieDto movieDto = new MovieDto.Builder()
                .withTitle(title)
                .withGenre(genre)
                .withLength(length)
                .build();
        movieService.updateMovie(movieDto);
    }
}
