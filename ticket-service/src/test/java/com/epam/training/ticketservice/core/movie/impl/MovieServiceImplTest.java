package com.epam.training.ticketservice.core.movie.impl;

import com.epam.training.ticketservice.core.mapper.MovieEntityToDtoMapper;
import com.epam.training.ticketservice.core.mapper.impl.MovieEntityToDtoMapperImpl;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

public class MovieServiceImplTest {
    private static final String TITLE = "Sprited Away";
    private static final String GENRE = "animation";
    private static final Integer LENGTH = 125;
    private static final Movie MOVIE_ENTITY = new Movie(null, "Sprited Away", "animation", 125);
    private static final MovieDto MOVIE = new MovieDto.Builder()
            .withTitle(TITLE)
            .withGenre(GENRE)
            .withLength(LENGTH)
            .build();


    private MovieServiceImpl underTest;
    private MovieRepository movieRepository;

    @BeforeEach
    public void init() {
        movieRepository = Mockito.mock(MovieRepository.class);
        MovieEntityToDtoMapper entityToDtoMapper = new MovieEntityToDtoMapperImpl();
        underTest = new MovieServiceImpl(movieRepository, entityToDtoMapper);
    }

    @Test
    public void testGetMovieListShouldCallMovieRepositoryAndReturnADtoList() {
        //Given
        Mockito.when(movieRepository
                .findAll())
                .thenReturn(List.of(MOVIE_ENTITY));
        List<MovieDto> expected = List.of(MOVIE);

        //When
        List<MovieDto> actual = underTest.getMovieList();

        //Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(movieRepository)
                .findAll();
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testExistByTitleShouldCallMovieRepositoryAndReturnTrueWhenTitleExist() {
        //Given
        Mockito.when(movieRepository
                .existsByTitle("Sprited Away"))
                .thenReturn(true);

        //When
        Boolean actual = underTest
                .existsByTitle("Sprited Away");

        //Then
        Assertions.assertTrue(actual);
        Mockito.verify(movieRepository)
                .existsByTitle("Sprited Away");
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testExistByTitleShouldCallMovieRepositoryAndReturnFalseWhenTitleNotExist() {
        //Given
        Mockito.when(movieRepository
                .existsByTitle("Sprited Away"))
                .thenReturn(false);

        //When
        Boolean actual = underTest
                .existsByTitle("Sprited Away");

        //Then
        Assertions.assertFalse(actual);
        Mockito.verify(movieRepository)
                .existsByTitle("Sprited Away");
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testCreateMovieShouldCallMovieRepositoryWhenTheInputMovieIsValid() {
        // Given
        Mockito.when(movieRepository
                .save(MOVIE_ENTITY))
                .thenReturn(MOVIE_ENTITY);
        Mockito.when(movieRepository
                .existsByTitle(MOVIE.getTitle()))
                .thenReturn(false);
        // When
        underTest.createMovie(MOVIE);

        // Then
        Mockito.verify(movieRepository)
                .save(MOVIE_ENTITY);
        Mockito.verify(movieRepository)
                .existsByTitle(MOVIE.getTitle());
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testCreateMovieShouldThrowIllegalArgumentExceptionWhenTheInputMovieIsInValid() {
        // Given
        Mockito.when(movieRepository
                .existsByTitle(MOVIE.getTitle()))
                .thenReturn(true);
        // When
        Assertions.assertThrows(IllegalArgumentException.class
                , () -> underTest.createMovie(MOVIE));

        // Then
        Mockito.verify(movieRepository)
                .existsByTitle(MOVIE.getTitle());
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenMovieIsNull() {
        // Given

        // When
        Assertions.assertThrows(NullPointerException.class
                , () -> underTest.createMovie(null));

        // Then
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenMovieTitleIsNull() {
        // Given
        MovieDto movie = new MovieDto.Builder()
                .withTitle(null)
                .withGenre("comedy")
                .withLength(120)
                .build();

        // When
        Assertions.assertThrows(NullPointerException.class
                , () -> underTest.createMovie(movie));

        // Then
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenMovieGenreIsNull() {
        // Given
        MovieDto movie = new MovieDto.Builder()
                .withTitle("Avangers")
                .withGenre(null)
                .withLength(200)
                .build();

        // When
        Assertions.assertThrows(NullPointerException.class
                , () -> underTest.createMovie(movie));

        // Then
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenMovieLengthIsNull() {
        // Given
        MovieDto movie = new MovieDto.Builder()
                .withTitle("Avangers")
                .withGenre("action")
                .withLength(null)
                .build();

        // When
        Assertions.assertThrows(NullPointerException.class
                , () -> underTest.createMovie(movie));

        // Then
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testFindByTitleShouldCallMovieRepositoryAndShouldReturnOptionalMovieDtoWhenTheInputIsValid() {
        //Given
        Mockito.when(movieRepository
                .findByTitle("Sprited Away"))
                .thenReturn(java.util.Optional.of(MOVIE_ENTITY));
        //When
        Optional<MovieDto> actual = underTest
                .findByTitle("Sprited Away");
        //Then
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(MOVIE, actual.get());

        Mockito.verify(movieRepository)
                .findByTitle("Sprited Away");
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testFindByTitleShouldCallMovieRepositoryAndShouldReturnOptionalEmptyWhenTheInputIsInValid() {
        //Given
        Mockito.when(movieRepository
                .findByTitle("Avangers"))
                .thenReturn(Optional.empty());
        //When
        Optional<MovieDto> actual = underTest
                .findByTitle("Avangers");
        //Then
        Assertions.assertFalse(actual.isPresent());
        Assertions.assertTrue(actual.isEmpty());

        Mockito.verify(movieRepository)
                .findByTitle("Avangers");
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testUpdateMovieShouldCallMovieRepositoryAndShouldModifyTheEntityWhenTheInputValid() {
        //Given
        Movie movie = new Movie(null
                , "Sprited Away"
                , "animation"
                , 125);
        Mockito.when(movieRepository
                .getMovieByTitle("Sprited Away"))
                .thenReturn(movie);
        Mockito.when(movieRepository
                .existsByTitle("Sprited Away"))
                .thenReturn(true);

        MovieDto requiredMovie = new MovieDto.Builder()
                .withTitle("Sprited Away")
                .withGenre("comedy")
                .withLength(400)
                .build();

        Movie expected = new Movie(null,
                requiredMovie.getTitle(),
                requiredMovie.getGenre(),
                requiredMovie.getLength());
        //When
        underTest.updateMovie(requiredMovie);

        //Then
        Assertions.assertEquals(expected, movieRepository
                .getMovieByTitle("Sprited Away"));
        Mockito.verify(movieRepository)
                .existsByTitle("Sprited Away");
        Mockito.verify(movieRepository, Mockito.times(2))
                .getMovieByTitle("Sprited Away");
        Mockito.verify(movieRepository)
                .save(expected);
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testUpdateMovieShouldCallMovieRepositoryAndShouldThrowIllegalArgumentExceptionWhenTheInputInValid() {
        //Given
        Mockito.when(movieRepository
                .existsByTitle("Sprited Away"))
                .thenReturn(false);

        MovieDto requiredMovie = new MovieDto.Builder()
                .withTitle("Sprited Away")
                .withGenre("comedy")
                .withLength(400)
                .build();
        //When
        Assertions.assertThrows(IllegalArgumentException.class
                , () -> underTest.updateMovie(requiredMovie));
        //Then
        Mockito.verify(movieRepository)
                .existsByTitle("Sprited Away");

        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testDeleteMovieShouldCallRepositoryWhenTheInputValidThenDeleteMovie() {
        //Given
        Mockito.when(movieRepository
                .existsByTitle("Sprited Away"))
                .thenReturn(true);
        //When
        underTest.deleteMovieByTitle("Sprited Away");
        //Then
        Mockito.verify(movieRepository)
                .existsByTitle("Sprited Away");
        Mockito.verify(movieRepository)
                .deleteByTitle("Sprited Away");

        Mockito.verifyNoMoreInteractions(movieRepository);

    }

    @Test
    public void testDeleteMovieShouldCallMovieRepositoryWhenTheInputInValidThenShouldThrowIllegalArgumentException() {
        //Given
        Mockito.when(movieRepository
                .existsByTitle("Sprited Away"))
                .thenReturn(false);

        //When
        Assertions.assertThrows(IllegalArgumentException.class
                , () -> underTest.deleteMovieByTitle("Sprited Away"));

        //Then
        Mockito.verify(movieRepository)
                .existsByTitle("Sprited Away");
        Mockito.verifyNoMoreInteractions(movieRepository);

    }
}
