package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.ui.utilities.out.helper.ConvertListToString;
import com.epam.training.ticketservice.ui.utilities.out.helper.ConvertListToStringImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.text.ParseException;
import java.util.List;

class MovieCommandsTest {
    ConvertListToString convertListToString;
    MovieService movieService;
    MovieCommands underTest;

    private static final String TITLE = "Sprited Away";
    private static final String GENRE = "animation";
    private static final Integer LENGTH = 125;
    private static final MovieDto MOVIE = new MovieDto.Builder()
            .withTitle(TITLE)
            .withGenre(GENRE)
            .withLength(LENGTH)
            .build();

    @BeforeEach
    public void init() throws ParseException {
        convertListToString = new ConvertListToStringImpl();
        movieService = Mockito.mock(MovieService.class);
        underTest = new MovieCommands(movieService, convertListToString);
    }

    @Test
    public void listMoviesShouldCallTheMovieServiceAndReturnAStringOfJoinedList(){
        //Given
        Mockito.when(movieService
                .getMovieList())
                .thenReturn(List.of(MOVIE));
        String expected = convertListToString.listToString(List.of(MOVIE));
        //When
        String actual = underTest.listOfMovies();
        //Then
        Assertions.assertEquals(expected,actual);

        Mockito.verify(movieService)
                .getMovieList();
        Mockito.verifyNoMoreInteractions(movieService);
    }

    @Test
    public void listMoviesShouldCallTheMovieServiceAndReturnThereAreNoMoviesAtTheMoment(){
        //Given
        Mockito.when(movieService
                .getMovieList())
                .thenReturn(List.of());
        String expected = "There are no movies at the moment";
        //When
        String actual = underTest.listOfMovies();
        //Then
        Assertions.assertEquals(expected,actual);

        Mockito.verify(movieService)
                .getMovieList();
        Mockito.verifyNoMoreInteractions(movieService);
    }

    @Test
    public void createMovieShouldCallTheMovieServiceAndReturnMovieWithThisNameAlreadyExistIfMovieAlreadyExist(){
        //Given
        Mockito.doThrow(new IllegalArgumentException("Movie with this title already exist")).when(movieService).createMovie(MOVIE);
        String expected = "Movie with this title already exist";
        //When
        String actual = underTest.createMovie(TITLE,GENRE,LENGTH);
        //Then
        Assertions.assertEquals(expected,actual);

        Mockito.verify(movieService)
                .createMovie(MOVIE);
        Mockito.verifyNoMoreInteractions(movieService);
    }

    @Test
    public void createMovieShouldCallTheMovieServiceAndReturnWithMovieDtoToString(){
        //Given
        String expected = "Create was successful";
        //When
        String actual = underTest.createMovie(TITLE,GENRE,LENGTH);
        //Then
        Assertions.assertEquals(expected,actual);

        Mockito.verify(movieService)
                .createMovie(MOVIE);
        Mockito.verifyNoMoreInteractions(movieService);
    }

    @Test
    public void deleteMovieByTitleShouldCallMovieServiceAndReturnDeleteWasSuccessFullIfMovieWithTitleExist(){
        //Given
        String expected = "Delete was successful";
        //When

        String actual = underTest.deleteMovieByTitle(TITLE);
        //Then
        Assertions.assertEquals(expected,actual);

        Mockito.verify(movieService)
                .deleteMovieByTitle(TITLE);
        Mockito.verifyNoMoreInteractions(movieService);
    }

    @Test
    public void deleteMovieByTitleShouldCallMovieServiceAndReturnIllegalArgumentExceptionWhenMovieWithTitleNotExist(){
        //Given
        Mockito.doThrow(new IllegalArgumentException("Movie with this title doesn't exist!"))
                .when(movieService).deleteMovieByTitle(TITLE);
        String expected = "Movie with this title doesn't exist!";
        //When
        String actual = underTest.deleteMovieByTitle(TITLE);

        //Then
        Assertions.assertEquals(expected,actual);

        Mockito.verify(movieService).deleteMovieByTitle(TITLE);
        Mockito.verifyNoMoreInteractions(movieService);
    }

    @Test
    public void updateMovieShouldCallMovieServiceAndThrowIllegalArgumentExceptionWhenTheMovieNotExist(){
        //Given
        Mockito.doThrow(new IllegalArgumentException("Movie with this title doesn't exist!"))
                .when(movieService).updateMovie(MOVIE);
        String expected = "Movie with this title doesn't exist!";

        //When
        String actual = underTest
                .updateMovie(TITLE,GENRE,LENGTH);
        //Then
        Assertions.assertEquals(expected,actual);
        Mockito.verify(movieService)
                .updateMovie(MOVIE);
        Mockito.verifyNoMoreInteractions(movieService);
    }

    @Test
    public void updateMovieShouldCallMovieServiceAndReturnUpdateSuccessIfUpdateSuccess(){
        //Given
        String expected = "Update Success";

        //When
        String actual = underTest.updateMovie(TITLE,GENRE,LENGTH);
        //Then
        Assertions.assertEquals(expected,actual);
        Mockito.verify(movieService).updateMovie(MOVIE);
        Mockito.verifyNoMoreInteractions(movieService);
    }

}