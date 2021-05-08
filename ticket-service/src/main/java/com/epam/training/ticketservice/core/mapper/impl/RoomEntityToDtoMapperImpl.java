package com.epam.training.ticketservice.core.mapper.impl;

import com.epam.training.ticketservice.core.mapper.RoomEntityToDtoMapper;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomEntityToDtoMapperImpl implements RoomEntityToDtoMapper {
    public RoomDto convertEntityToDto(Room room) {
        return new RoomDto.Builder()
                .withRoomName(room.getName())
                .withSeatRows(room.getSeatRows())
                .withSeatColumns(room.getSeatColumns())
                .build();
    }
}
