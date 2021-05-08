package com.epam.training.ticketservice.core.room.impl;

import com.epam.training.ticketservice.core.mapper.RoomEntityToDtoMapper;
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
    private final RoomEntityToDtoMapper entityToDtoMapper;

    RoomServiceImpl(RoomRepository roomRepository, RoomEntityToDtoMapper entityToDtoMapper) {
        this.roomRepository = roomRepository;
        this.entityToDtoMapper = entityToDtoMapper;
    }

    @Override
    public List<RoomDto> getRoomList() {
        return roomRepository.findAll().stream()
                .map(entityToDtoMapper::convertEntityToDto)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void createRoom(RoomDto roomDto) {
        Objects.requireNonNull(roomDto, "Room cannot be null!");
        Objects.requireNonNull(roomDto.getRoomName(), "Room name cannot be null!");
        Objects.requireNonNull(roomDto.getSeatColumns(), "Room's seat columns cannot be null!");
        Objects.requireNonNull(roomDto.getSeatRows(), "Room's seat rows cannot be null!");
        if (roomRepository.existsByName(roomDto.getRoomName())) {
            throw new IllegalArgumentException("Room with this name already exist");
        }
        roomRepository.save(new Room(null,
                    roomDto.getRoomName(),
                    roomDto.getSeatRows(),
                    roomDto.getSeatColumns()));
    }

    @Override
    public void updateRoom(RoomDto roomDto) {
        if (!roomRepository.existsByName(roomDto.getRoomName())) {
            throw new IllegalArgumentException("Room with this name doesn't exist!");
        }
        Room room = roomRepository.getRoomByName(roomDto.getRoomName());
        room.setSeatColumns(roomDto.getSeatColumns());
        room.setSeatRows(roomDto.getSeatRows());
        roomRepository.save(room);
    }

    @Override
    public void deleteRoomByName(String name) {
        Objects.requireNonNull(name, "RoomName cannot be null!");
        if (!roomRepository.existsByName(name)) {
            throw new IllegalArgumentException("Room with this name doesn't exist!");
        }
        roomRepository.deleteByName(name);
    }

    @Override
    public boolean existsByName(String name) {
        return roomRepository.existsByName(name);
    }
}
