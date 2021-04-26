package com.epam.training.ticketservice.core.room.impl;

import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public List<RoomDto> getRoomList() {
        return roomRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public void createRoom(RoomDto roomDto) {
        Objects.requireNonNull(roomDto,"Room cannot be null!");
        Objects.requireNonNull(roomDto.getRoomName(),"Room name cannot be null!");
        Objects.requireNonNull(roomDto.getSeatColumns(),"Room's seat columns cannot be null!");
        Objects.requireNonNull(roomDto.getSeatRows(),"Room's seat rows cannot be null!");

        roomRepository.save(new Room(null,
                roomDto.getRoomName(),
                roomDto.getSeatRows(),
                roomDto.getSeatColumns()));
    }

    @Override
    public void updateRoom(RoomDto roomDto) {

    }

    @Override
    public void deleteRoomByName(String name) {
        roomRepository.deleteByName(name);
    }

    private RoomDto convertEntityToDto(Room room){
        return new RoomDto.Builder()
                .withRoomName(room.getName())
                .withSeatRows(room.getSeatRows())
                .withSeatColumns(room.getSeatColumns())
                .build();
    }
}
