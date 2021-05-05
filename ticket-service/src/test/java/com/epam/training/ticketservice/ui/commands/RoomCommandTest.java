package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.ui.utilities.out.helper.ConvertListToString;
import com.epam.training.ticketservice.ui.utilities.out.helper.ConvertListToStringImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomCommandTest {

    private static final String ROOM_NAME = "A1";
    private static final Integer ROWS = 10;
    private static final Integer COLUMNS = 10;
    private static final RoomDto ROOM = new RoomDto.Builder()
            .withRoomName(ROOM_NAME)
            .withSeatRows(ROWS)
            .withSeatColumns(COLUMNS)
            .build();

    ConvertListToString convertListToString;
    RoomService roomService;
    RoomCommand underTest;

    @BeforeEach
    public void init() throws ParseException {
        roomService= Mockito.mock(RoomService.class);
        convertListToString = new ConvertListToStringImpl();
        underTest=new RoomCommand(roomService,convertListToString);
    }

    @Test
    public void listRoomsShouldCallTheRoomServiceAndReturnAStringOfJoinedList(){
        //Given
        Mockito.when(roomService
                .getRoomList())
                .thenReturn(List.of(ROOM));
        String expected = convertListToString.listToString(List.of(ROOM));
        //When
        String actual = underTest.getRoomList();
        //Then
        Assertions.assertEquals(expected,actual);

        Mockito.verify(roomService)
                .getRoomList();
        Mockito.verifyNoMoreInteractions(roomService);
    }

    @Test
    public void listRoomsShouldCallTheRoomServiceAndReturnThereAreNoRoomsAtTheMoment(){
        //Given
        Mockito.when(roomService
                .getRoomList())
                .thenReturn(List.of());
        String expected = "There are no rooms at the moment";
        //When
        String actual = underTest.getRoomList();
        //Then
        Assertions.assertEquals(expected,actual);

        Mockito.verify(roomService)
                .getRoomList();
        Mockito.verifyNoMoreInteractions(roomService);
    }

    @Test
    public void createRoomShouldCallTheRoomServiceAndReturnRoomWithThisNameAlreadyExistIfRoomAlreadyExist(){
        //Given
        Mockito.doThrow(new IllegalArgumentException("Room with this name already exist")).when(roomService)
                .createRoom(ROOM);
        String expected = "Room with this name already exist";
        //When
        String actual = underTest.createRoom(ROOM_NAME,ROWS,COLUMNS);
        //Then
        Assertions.assertEquals(expected,actual);

        Mockito.verify(roomService)
                .createRoom(ROOM);
        Mockito.verifyNoMoreInteractions(roomService);
    }

    @Test
    public void createRoomShouldCallTheRoomServiceAndReturnWithRoomDtoToString(){
        //Given
        String expected = ROOM.toString();
        //When
        String actual = underTest.createRoom(ROOM_NAME,ROWS,COLUMNS);
        //Then
        Assertions.assertEquals(expected,actual);

        Mockito.verify(roomService)
                .createRoom(ROOM);
        Mockito.verifyNoMoreInteractions(roomService);
    }

    @Test
    public void deleteRoomByNameShouldCallRoomServiceAndReturnDeleteWasSuccessFullIfRoomWithNameExist(){
        //Given
        String expected = "Delete was successful";
        //When

        String actual = underTest.deleteRoom(ROOM_NAME);
        //Then
        Assertions.assertEquals(expected,actual);

        Mockito.verify(roomService)
                .deleteRoomByName(ROOM_NAME);
        Mockito.verifyNoMoreInteractions(roomService);
    }

    @Test
    public void deleteRoomByNameShouldCallRoomServiceAndReturnIllegalArgumentExceptionWhenRoomWithNameNotExist(){
        //Given
        Mockito.doThrow(new IllegalArgumentException("Room with this name doesn't exist!"))
                .when(roomService).deleteRoomByName(ROOM_NAME);
        String expected = "Room with this name doesn't exist!";
        //When
        String actual = underTest.deleteRoom(ROOM_NAME);

        //Then
        Assertions.assertEquals(expected,actual);

        Mockito.verify(roomService).deleteRoomByName(ROOM_NAME);
        Mockito.verifyNoMoreInteractions(roomService);
    }

    @Test
    public void updateRoomShouldCallRoomServiceAndThrowIllegalArgumentExceptionWhenTheRoomNotExist(){
        //Given
        Mockito.doThrow(new IllegalArgumentException("Room with this name doesn't exist!"))
                .when(roomService).updateRoom(ROOM);
        String expected = "Room with this name doesn't exist!";

        //When
        String actual = underTest
                .updateRoom(ROOM_NAME,ROWS,COLUMNS);
        //Then
        Assertions.assertEquals(expected,actual);
        Mockito.verify(roomService)
                .updateRoom(ROOM);
        Mockito.verifyNoMoreInteractions(roomService);
    }

    @Test
    public void updateRoomShouldCallRoomServiceAndReturnUpdateWasSuccessfulIfUpdateSuccess(){
        //Given
        String expected = "Update was successful";

        //When
        String actual = underTest.updateRoom(ROOM_NAME,ROWS,COLUMNS);
        //Then
        Assertions.assertEquals(expected,actual);
        Mockito.verify(roomService).updateRoom(ROOM);
        Mockito.verifyNoMoreInteractions(roomService);
    }

}