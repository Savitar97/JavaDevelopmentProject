package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;

@ShellComponent
public class MovieCommands {
    private final MovieService movieService;

    public MovieCommands(MovieService movieService) {
        this.movieService = movieService;
    }

    @ShellMethod(value="Movie List",key="list movies")
    public List<MovieDto> listOfMovies(){
        return movieService.getMovieList();
    }

    @ShellMethod(value="Create Movie",key="create movie")
    public MovieDto createMovie(String title,String genre,Integer length){
        MovieDto movieDto=new MovieDto.Builder()
                .withTitle(title)
                .withGenre(genre)
                .withLength(length)
                .build();
        movieService.createMovie(movieDto);
        return movieDto;
    }

    @ShellMethod(value="Delete Movie",key="delete movie")
    public void deleteMovieByTitle(String title){
        movieService.deleteMovieByTitle(title);
    }

    @ShellMethod(value="Update Movie",key="update movie")
    public void updateMovie(String title,String genre,Integer length){
        MovieDto movieDto=new MovieDto.Builder()
                .withTitle(title)
                .withGenre(genre)
                .withLength(length)
                .build();
        movieService.updateMovie(movieDto);
    }
}
