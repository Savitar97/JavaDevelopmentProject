package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.entity.ScreeningId;
import com.epam.training.ticketservice.ui.utilities.StringToDate;
import com.epam.training.ticketservice.ui.utilities.out.helper.ConvertListToString;
import com.epam.training.ticketservice.ui.utilities.out.helper.ConvertListToStringImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

class ScreeningCommandTest {
    private ScreeningService screeningService;
    private ConvertListToString convertListToString;
    private ScreeningCommand underTest;

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

    private static final Room ROOM_ENTITY = new Room(null, "A1", 10, 10, null);
    private static final Movie MOVIE_ENTITY = new Movie(null, "Sprited Away", "animation", 125, null);
    private static final ScreeningId SCREENING_ID = new ScreeningId(MOVIE_ENTITY, ROOM_ENTITY, DATE);
    private static final Screening SCREENING_ENTITY = new Screening(SCREENING_ID, null);
    private static final ScreeningDto SCREENING_DTO = ScreeningDto.builder()
            .room(ROOM)
            .movie(MOVIE)
            .startTime(DATE)
            .build();


    @BeforeEach
    public void init() throws ParseException {
        screeningService = Mockito.mock(ScreeningService.class);
        convertListToString = new ConvertListToStringImpl();
        underTest = new ScreeningCommand(screeningService, convertListToString);
    }

    @Test
    public void testCreateScreeningShouldCreateScreeningIfInputValid() {
        //Given
        String expected = "Create was successful";
        //When
        String actual = underTest.createScreening(TITLE,ROOM_NAME,DATE);
        //Then
        Assertions.assertEquals(expected,actual);
        Mockito.verify(screeningService).createScreening(TITLE,ROOM_NAME,DATE);
        Mockito.verifyNoMoreInteractions(screeningService);
    }

    @Test
    public void testCreateScreeningShouldThrowIllegalArgumentExceptionWhenInputInvalid(){
        //Given
        Mockito.doThrow(new IllegalArgumentException("Screening already exist")).when(screeningService)
                .createScreening(TITLE,ROOM_NAME,DATE);
        String expected = "Screening already exist";
        //When
        String actual = underTest.createScreening(TITLE,ROOM_NAME,DATE);
        //Then
        Assertions.assertEquals(expected,actual);
        Mockito.verify(screeningService).createScreening(TITLE,ROOM_NAME,DATE);
        Mockito.verifyNoMoreInteractions(screeningService);

    }

    @Test
    public void testDeleteScreeningShouldDeleteScreeningIfInputValid() {
        //Given
        String expected = "Delete was successful";
        //When
        String actual = underTest.deleteScreening(TITLE,ROOM_NAME,DATE);
        //Then
        Assertions.assertEquals(expected,actual);
        Mockito.verify(screeningService).deleteScreening(TITLE,ROOM_NAME,DATE);
        Mockito.verifyNoMoreInteractions(screeningService);
    }

    @Test
    public void testDeleteScreeningShouldThrowIllegalArgumentExceptionWhenInputInvalid(){
        //Given
        Mockito.doThrow(new IllegalArgumentException("Screening not exist")).when(screeningService)
                .deleteScreening(TITLE,ROOM_NAME,DATE);
        String expected = "Screening not exist";
        //When
        String actual = underTest.deleteScreening(TITLE,ROOM_NAME,DATE);
        //Then
        Assertions.assertEquals(expected,actual);
        Mockito.verify(screeningService).deleteScreening(TITLE,ROOM_NAME,DATE);
        Mockito.verifyNoMoreInteractions(screeningService);

    }

    @Test
    public void testListScreeningShouldReturnThereAreNoScreeningsWhenScreeningListEmpty(){
        //Given
        Mockito.when(screeningService.getScreening()).thenReturn(List.of());
        String expected = "There are no screenings";
        //When
        String actual = underTest.getScreeningList();
        //Then
        Assertions.assertEquals(expected,actual);
        Mockito.verify(screeningService).getScreening();
        Mockito.verifyNoMoreInteractions(screeningService);
    }

    @Test
    public void testListScreeningShouldReturnListOfScreenings(){
        //Given
        Mockito.when(screeningService.getScreening()).thenReturn(List.of(SCREENING_DTO));
        String expected = convertListToString.listToString(List.of(SCREENING_DTO));
        //When
        String actual = underTest.getScreeningList();
        //Then
        Assertions.assertEquals(expected,actual);
        Mockito.verify(screeningService).getScreening();
        Mockito.verifyNoMoreInteractions(screeningService);
    }


}