package com.epam.training.ticketservice.core.screening.impl;

import com.epam.training.ticketservice.core.mapper.EntityToDtoMapper;
import com.epam.training.ticketservice.core.mapper.impl.EntityToDtoMapperImpl;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.impl.RoomServiceImpl;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.entity.ScreeningId;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import com.epam.training.ticketservice.ui.utilities.StringToDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;


class ScreeningServiceImplTest {
    private static final String DATE_STRING = "2021-03-14 16:00";
    private static final StringToDate stringToDate = new StringToDate();
    private static final Date DATE = stringToDate.convert(DATE_STRING);


    private static final String ROOM_NAME = "A1";
    private static final Integer ROWS = 10;
    private static final Integer COLUMNS = 10;
    private static final RoomDto ROOM = new RoomDto.Builder()
            .withRoomName(ROOM_NAME)
            .withSeatRows(ROWS)
            .withSeatColumns(COLUMNS)
            .build();

    private static final String TITLE = "Sprited Away";
    private static final String GENRE = "animation";
    private static final Integer LENGTH = 125;
    private static final MovieDto MOVIE = new MovieDto.Builder()
            .withTitle(TITLE)
            .withGenre(GENRE)
            .withLength(LENGTH)
            .build();

    private static final Room ROOM_ENTITY = new Room(null, "A1", 10, 10);
    private static final Movie MOVIE_ENTITY = new Movie(null, "Sprited Away", "animation", 125);
    private static final ScreeningId SCREENING_ID = new ScreeningId(MOVIE_ENTITY, ROOM_ENTITY, DATE);
    private static final Screening SCREENING_ENTITY = new Screening(SCREENING_ID);


    private ScreeningServiceImpl underTest;
    private ScreeningRepository screeningRepository;
    private RoomRepository roomRepository;
    private MovieRepository movieRepository;

    private static final ScreeningDto SCREENING = ScreeningDto.builder()
            .movie(MOVIE)
            .room(ROOM)
            .startTime(DATE)
            .build();

    @BeforeEach
    public void init() throws ParseException {

        EntityToDtoMapper entityToDtoMapper = new EntityToDtoMapperImpl();
        screeningRepository = Mockito.mock(ScreeningRepository.class);
        roomRepository = Mockito.mock(RoomRepository.class);
        movieRepository = Mockito.mock(MovieRepository.class);
        underTest = new ScreeningServiceImpl(roomRepository,
                movieRepository,
                screeningRepository,
                entityToDtoMapper);
    }

    @Test
    public void testGetScreeningListShouldCallScreeningRepositoryAndReturnADtoList() {
        //Given
        Mockito.when(screeningRepository.findAll())
                .thenReturn(List.of(SCREENING_ENTITY));
        List<ScreeningDto> expected = List.of(SCREENING);
        //When
        List<ScreeningDto> actual = underTest.getScreening();

        //Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(screeningRepository)
                .findAll();
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    public void testCreateScreeningShouldCallMovieRepositoryAndRoomRepositoryAndScreeningRepositoryWhenTheInputIsValidAndNotHaveOverlapping() {
        //Given
        Mockito.when(movieRepository
                .existsByTitle(TITLE))
                .thenReturn(true);
        Mockito.when(roomRepository
                .existsByName(ROOM_NAME))
                .thenReturn(true);
        Mockito.when(screeningRepository
                .existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE
                        , ROOM_NAME, DATE))
                .thenReturn(false);
        Mockito.when(screeningRepository
                .getAllByIdRoomNameEquals(ROOM_NAME))
                .thenReturn(List.of());
        Mockito.when(screeningRepository
                .save(SCREENING_ENTITY))
                .thenReturn(SCREENING_ENTITY);
        Mockito.when(movieRepository
                .getMovieByTitle(TITLE))
                .thenReturn(MOVIE_ENTITY);
        Mockito.when(roomRepository
                .findByName(ROOM_NAME))
                .thenReturn(ROOM_ENTITY);
        //When
        Assertions.assertEquals(SCREENING_ENTITY.toString()
                , underTest.createScreening(TITLE, ROOM_NAME, DATE));
        //Then
        Mockito.verify(movieRepository)
                .existsByTitle(TITLE);
        Mockito.verify(roomRepository)
                .existsByName(ROOM_NAME);
        Mockito.verify(screeningRepository)
                .existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE, ROOM_NAME, DATE);
        Mockito.verify(screeningRepository)
                .getAllByIdRoomNameEquals(ROOM_NAME);
        Mockito.verify(screeningRepository)
                .save(SCREENING_ENTITY);
        Mockito.verify(movieRepository)
                .getMovieByTitle(TITLE);
        Mockito.verify(roomRepository)
                .findByName(ROOM_NAME);
        Mockito.verifyNoMoreInteractions(movieRepository);
        Mockito.verifyNoMoreInteractions(roomRepository);
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    public void testCreateScreeningShouldCallMovieRepositoryAndRoomRepositoryAndScreeningRepositoryWhenTheInputIsValidAndNotHaveOverlappingAndHaveAnotherScreening() {
        //Given
        ScreeningId screeningId = new ScreeningId(MOVIE_ENTITY,
                ROOM_ENTITY,
                stringToDate.convert("2021-03-14 13:45"));
        Screening screening = new Screening(screeningId);
        Mockito.when(movieRepository
                .existsByTitle(TITLE))
                .thenReturn(true);
        Mockito.when(roomRepository
                .existsByName(ROOM_NAME))
                .thenReturn(true);
        Mockito.when(screeningRepository
                .existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE, ROOM_NAME
                        , DATE)).thenReturn(false);
        Mockito.when(screeningRepository
                .getAllByIdRoomNameEquals(ROOM_NAME))
                .thenReturn(List.of(screening));
        Mockito.when(screeningRepository
                .save(SCREENING_ENTITY))
                .thenReturn(SCREENING_ENTITY);
        Mockito.when(movieRepository
                .getMovieByTitle(TITLE))
                .thenReturn(MOVIE_ENTITY);
        Mockito.when(roomRepository
                .findByName(ROOM_NAME))
                .thenReturn(ROOM_ENTITY);


        //When
        underTest.createScreening(TITLE, ROOM_NAME, DATE);
        //Then
        Mockito.verify(movieRepository)
                .existsByTitle(TITLE);
        Mockito.verify(roomRepository)
                .existsByName(ROOM_NAME);
        Mockito.verify(screeningRepository)
                .existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE
                        , ROOM_NAME, DATE);
        Mockito.verify(screeningRepository)
                .getAllByIdRoomNameEquals(ROOM_NAME);
        Mockito.verify(screeningRepository)
                .save(SCREENING_ENTITY);
        Mockito.verify(movieRepository)
                .getMovieByTitle(TITLE);
        Mockito.verify(roomRepository)
                .findByName(ROOM_NAME);
        Mockito.verifyNoMoreInteractions(movieRepository);
        Mockito.verifyNoMoreInteractions(roomRepository);
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    public void testCreateScreeningShouldThrowIllegalArgumentExceptionWhenThereIsOverlapping() {
        //Given
        ScreeningId screeningId = new ScreeningId(MOVIE_ENTITY,
                ROOM_ENTITY,
                stringToDate.convert("2021-03-14 16:00"));

        Screening screening = new Screening(screeningId);

        Mockito.when(movieRepository
                .existsByTitle(TITLE))
                .thenReturn(true);
        Mockito.when(roomRepository
                .existsByName(ROOM_NAME))
                .thenReturn(true);
        Mockito.when(screeningRepository
                .existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE, ROOM_NAME
                        , DATE)).thenReturn(false);
        Mockito.when(screeningRepository
                .getAllByIdRoomNameEquals(ROOM_NAME))
                .thenReturn(List.of(screening));
        Mockito.when(movieRepository
                .getMovieByTitle(TITLE))
                .thenReturn(MOVIE_ENTITY);
        Mockito.when(roomRepository
                .findByName(ROOM_NAME))
                .thenReturn(ROOM_ENTITY);


        //When
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> underTest.createScreening(TITLE, ROOM_NAME, DATE));
        //Then
        Mockito.verify(movieRepository)
                .existsByTitle(TITLE);
        Mockito.verify(roomRepository)
                .existsByName(ROOM_NAME);
        Mockito.verify(screeningRepository)
                .existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE
                        , ROOM_NAME, DATE);
        Mockito.verify(screeningRepository)
                .getAllByIdRoomNameEquals(ROOM_NAME);
        Mockito.verify(movieRepository)
                .getMovieByTitle(TITLE);
        Mockito.verify(roomRepository)
                .findByName(ROOM_NAME);

        Mockito.verifyNoMoreInteractions(movieRepository);
        Mockito.verifyNoMoreInteractions(roomRepository);
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    public void testCreateScreeningShouldThrowIllegalArgumentExceptionWhenThereIsOverlappingInBreakTime() {
        //Given
        ScreeningId screeningId = new ScreeningId(MOVIE_ENTITY,
                ROOM_ENTITY,
                stringToDate.convert("2021-03-14 13:50"));
        Screening screening = new Screening(screeningId);

        Mockito.when(movieRepository
                .existsByTitle(TITLE))
                .thenReturn(true);
        Mockito.when(roomRepository
                .existsByName(ROOM_NAME))
                .thenReturn(true);
        Mockito.when(screeningRepository
                .existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE, ROOM_NAME
                        , DATE)).thenReturn(false);
        Mockito.when(screeningRepository
                .getAllByIdRoomNameEquals(ROOM_NAME))
                .thenReturn(List.of(screening));
        Mockito.when(movieRepository
                .getMovieByTitle(TITLE))
                .thenReturn(MOVIE_ENTITY);
        Mockito.when(roomRepository
                .findByName(ROOM_NAME))
                .thenReturn(ROOM_ENTITY);

        //When
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> underTest.createScreening(TITLE, ROOM_NAME, DATE));


        //Then
        Mockito.verify(movieRepository)
                .existsByTitle(TITLE);
        Mockito.verify(roomRepository)
                .existsByName(ROOM_NAME);
        Mockito.verify(screeningRepository)
                .existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE
                        , ROOM_NAME, DATE);
        Mockito.verify(screeningRepository)
                .getAllByIdRoomNameEquals(ROOM_NAME);
        Mockito.verify(movieRepository)
                .getMovieByTitle(TITLE);
        Mockito.verify(roomRepository)
                .findByName(ROOM_NAME);

        Mockito.verifyNoMoreInteractions(movieRepository);
        Mockito.verifyNoMoreInteractions(roomRepository);
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    public void testCreateScreeningShouldThrowIllegalArgumentExceptionWhenMovieByTitleNotExist() {
        //given
        Mockito.when(movieRepository.existsByTitle(TITLE)).thenReturn(false);
        //When
        Assertions.assertThrows(IllegalArgumentException.class, () -> underTest.createScreening(TITLE, ROOM_NAME, DATE));
        //Then
        Mockito.verify(movieRepository).existsByTitle(TITLE);

        Mockito.verifyNoMoreInteractions(movieRepository);
        Mockito.verifyNoMoreInteractions(roomRepository);
        Mockito.verifyNoMoreInteractions(screeningRepository);

    }

    @Test
    public void testCreateScreeningShouldThrowIllegalArgumentExceptionWhenMovieByTitleExistButRoomByNameNotExist() {
        //given
        Mockito.when(movieRepository
                .existsByTitle(TITLE))
                .thenReturn(true);
        Mockito.when(roomRepository
                .existsByName(ROOM_NAME))
                .thenReturn(false);

        //When
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> underTest.createScreening(TITLE, ROOM_NAME, DATE));

        //Then
        Mockito.verify(movieRepository)
                .existsByTitle(TITLE);
        Mockito.verify(roomRepository)
                .existsByName(ROOM_NAME);

        Mockito.verifyNoMoreInteractions(movieRepository);
        Mockito.verifyNoMoreInteractions(roomRepository);
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    public void testCreateScreeningShouldThrowIllegalArgumentExceptionWhenMovieByTitleExistAndRoomByNameExistAndScreeningAlreadyExist() {
        //Given
        Mockito.when(movieRepository
                .existsByTitle(TITLE))
                .thenReturn(true);
        Mockito.when(roomRepository
                .existsByName(ROOM_NAME))
                .thenReturn(true);
        Mockito.when(screeningRepository
                .existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE
                        , ROOM_NAME, DATE))
                .thenReturn(true);

        //When
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> underTest.createScreening(TITLE, ROOM_NAME, DATE));

        //Then
        Mockito.verify(movieRepository)
                .existsByTitle(TITLE);
        Mockito.verify(roomRepository)
                .existsByName(ROOM_NAME);
        Mockito.verify(screeningRepository)
                .existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE, ROOM_NAME, DATE);


        Mockito.verifyNoMoreInteractions(movieRepository);
        Mockito.verifyNoMoreInteractions(roomRepository);
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    public void testCreateScreenShouldThrowNullPointerExceptionWhenMovieTitleArgumentNull() {
        //When
        Assertions.assertThrows(NullPointerException.class
                , () -> underTest.createScreening(null, ROOM_NAME, DATE));
        //Then
        Mockito.verifyNoMoreInteractions(movieRepository);
        Mockito.verifyNoMoreInteractions(roomRepository);
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    public void testCreateScreenShouldThrowNullPointerExceptionWhenRoomNameArgumentNull() {
        //When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.createScreening(TITLE, null, DATE));
        //Then
        Mockito.verifyNoMoreInteractions(movieRepository);
        Mockito.verifyNoMoreInteractions(roomRepository);
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    public void testCreateScreenShouldThrowNullPointerExceptionWhenStartTimeArgumentNull() {
        //When
        Assertions.assertThrows(NullPointerException.class
                , () -> underTest.createScreening(TITLE, ROOM_NAME, null));
        //Then
        Mockito.verifyNoMoreInteractions(movieRepository);
        Mockito.verifyNoMoreInteractions(roomRepository);
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    public void testDeleteShouldThrowIllegalArgumentExceptionIfTheScreeningNotExist() {
        //Given
        Mockito.when(screeningRepository
                .existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE))
                .thenReturn(false);

        //When
        Assertions.assertThrows(IllegalArgumentException.class
                ,()->underTest.deleteScreening(TITLE,ROOM_NAME,DATE));
        //Then
        Mockito.verify(screeningRepository)
                .existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE);
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    public void testDeleteScreeningShouldDeleteScreeningWhenTheScreeningExist(){
        //Given
        Mockito.when(screeningRepository
                .existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE))
                .thenReturn(true);
        //When
        underTest.deleteScreening(TITLE,ROOM_NAME,DATE);
        //Then
        Mockito.verify(screeningRepository)
                .existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE);
        Mockito.verify(screeningRepository)
                .deleteScreeningById_Movie_TitleAndId_Room_NameAndAndId_StartTime(TITLE,ROOM_NAME,DATE);

        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

}