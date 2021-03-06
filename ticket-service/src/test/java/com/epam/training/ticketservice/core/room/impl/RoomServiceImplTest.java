package com.epam.training.ticketservice.core.room.impl;

import com.epam.training.ticketservice.core.mapper.RoomEntityToDtoMapper;
import com.epam.training.ticketservice.core.mapper.impl.RoomEntityToDtoMapperImpl;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;


class RoomServiceImplTest {
    private RoomServiceImpl underTest;
    private RoomRepository roomRepository;

    private static final String ROOM_NAME = "A1";
    private static final Integer ROWS = 10;
    private static final Integer COLUMNS = 10;
    private static final Room ROOM_ENTITY = new Room(null, "A1", 10, 10, null);
    private static final RoomDto ROOM = new RoomDto.Builder()
            .withRoomName(ROOM_NAME)
            .withSeatRows(ROWS)
            .withSeatColumns(COLUMNS)
            .build();

    @BeforeEach
    public void init() {
        RoomEntityToDtoMapper entityToDtoMapper = new RoomEntityToDtoMapperImpl();
        roomRepository = Mockito.mock(RoomRepository.class);
        underTest = new RoomServiceImpl(roomRepository, entityToDtoMapper);
    }

    @Test
    public void testGetRoomListShouldCallRoomRepositoryAndReturnADtoList() {
        //Given
        Mockito.when(roomRepository.findAll()).thenReturn(List.of(ROOM_ENTITY));
        List<RoomDto> expected = List.of(ROOM);

        //When
        List<RoomDto> actual = underTest.getRoomList();

        //Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(roomRepository).findAll();
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testExistByNameShouldCallRoomRepositoryAndReturnTrueWhenRoomNameExist() {
        //Given
        Mockito.when(roomRepository.existsByName("A1")).thenReturn(true);
        //When
        boolean actual = underTest.existsByName("A1");
        //Then
        Assertions.assertTrue(actual);
        Mockito.verify(roomRepository).existsByName("A1");
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testExistByNameShouldCallRoomRepositoryAndReturnFalseWhenRoomNameNotExist() {
        //Given
        Mockito.when(roomRepository.existsByName("A1")).thenReturn(false);
        //When
        boolean actual = underTest.existsByName("A1");
        //Then
        Assertions.assertFalse(actual);
        Mockito.verify(roomRepository).existsByName("A1");
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testCreateRoomShouldCallRoomRepositoryWhenTheInputRoomIsValid() {
        //Given
        Mockito.when(roomRepository
                .save(ROOM_ENTITY))
                .thenReturn(ROOM_ENTITY);
        Mockito.when(roomRepository
                .existsByName(ROOM.getRoomName()))
                .thenReturn(false);
        //When
        underTest.createRoom(ROOM);
        //Then
        Mockito.verify(roomRepository)
                .existsByName(ROOM.getRoomName());
        Mockito.verify(roomRepository)
                .save(ROOM_ENTITY);
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testCreateRoomShouldThrowIllegalArgumentExceptionWhenTheInputRoomIsInValid() {
        //Given
        Mockito.when(roomRepository
                .existsByName(ROOM.getRoomName()))
                .thenReturn(true);
        //When
        Assertions.assertThrows(IllegalArgumentException.class
                , () -> underTest.createRoom(ROOM));
        //Then
        Mockito.verify(roomRepository)
                .existsByName(ROOM.getRoomName());
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testCreateRoomShouldThrowNullPointerExceptionWhenRoomIsNull() {
        //When
        Assertions.assertThrows(NullPointerException.class
                , () -> underTest.createRoom(null));
        //Then
        Mockito.verifyNoMoreInteractions(roomRepository);

    }

    @Test
    public void testCreateRoomShouldThrowNullPointerExceptionWhenRoomNameIsNull() {
        //Given
        RoomDto roomDto = new RoomDto.Builder()
                .withRoomName(null)
                .withSeatColumns(10)
                .withSeatRows(10)
                .build();
        //When
        Assertions.assertThrows(NullPointerException.class
                , () -> underTest.createRoom(roomDto));
        //Then
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testCreateRoomShouldThrowNullPointerExceptionWhenSeatColumnsIsNull() {
        //Given
        RoomDto roomDto = new RoomDto.Builder()
                .withRoomName("A1")
                .withSeatColumns(null)
                .withSeatRows(10)
                .build();
        //When
        Assertions.assertThrows(NullPointerException.class
                , () -> underTest.createRoom(roomDto));
        //Then
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testCreateRoomShouldThrowNullPointerExceptionWhenSeatRowsIsNull() {
        //Given
        RoomDto roomDto = new RoomDto.Builder()
                .withRoomName("A1")
                .withSeatColumns(10)
                .withSeatRows(null)
                .build();
        //When
        Assertions.assertThrows(NullPointerException.class
                , () -> underTest.createRoom(roomDto));
        //Then
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testDeleteRoomShouldCallRoomRepositoryWhenTheInputValidThenDeleteMovie() {
        //Given
        Mockito.when(roomRepository
                .existsByName("A1"))
                .thenReturn(true);
        //When
        underTest.deleteRoomByName("A1");

        //Then
        Mockito.verify(roomRepository)
                .existsByName("A1");
        Mockito.verify(roomRepository)
                .deleteByName("A1");

        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testDeleteRoomShouldCallRoomRepositoryWhenTheInputInValidThenShouldThrowIllegalArgumentException() {
        //Given
        Mockito.when(roomRepository.existsByName("A1"))
                .thenReturn(false);
        //When
        Assertions.assertThrows(IllegalArgumentException.class
                , () -> underTest.deleteRoomByName("A1"));
        //Then
        Mockito.verify(roomRepository)
                .existsByName("A1");
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testUpdateRoomShouldCallRoomRepositoryAndShouldThrowIllegalArgumentExceptionWhenTheInputInValid() {
        //Given
        Mockito.when(roomRepository.existsByName("A1"))
                .thenReturn(false);

        RoomDto requiredRoom = new RoomDto.Builder()
                .withRoomName("A1")
                .withSeatColumns(10)
                .withSeatRows(10)
                .build();
        //When
        Assertions.assertThrows(IllegalArgumentException.class
                , () -> underTest.updateRoom(requiredRoom));

        //Then
        Mockito.verify(roomRepository)
                .existsByName("A1");
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testUpdateRoomShouldCallRoomRepositoryAndShouldModifyTheEntityWhenTheInputValid() {
        //Given
        Room room = new Room(null, "A1", 10, 10, null);

        Mockito.when(roomRepository
                .existsByName("A1"))
                .thenReturn(true);
        Mockito.when(roomRepository
                .getRoomByName("A1"))
                .thenReturn(room);

        RoomDto requiredRoom = new RoomDto.Builder()
                .withRoomName("A1")
                .withSeatColumns(20)
                .withSeatRows(20)
                .build();

        Room expected = new Room(null,
                requiredRoom.getRoomName(),
                requiredRoom.getSeatRows(),
                requiredRoom.getSeatColumns(), null);
        //When

        underTest.updateRoom(requiredRoom);


        //Then
        Assertions.assertEquals(expected, roomRepository
                .getRoomByName("A1"));
        Mockito.verify(roomRepository)
                .existsByName("A1");
        Mockito.verify(roomRepository, Mockito.times(2))
                .getRoomByName("A1");
        Mockito.verify(roomRepository)
                .save(expected);

        Mockito.verifyNoMoreInteractions(roomRepository);

    }
}