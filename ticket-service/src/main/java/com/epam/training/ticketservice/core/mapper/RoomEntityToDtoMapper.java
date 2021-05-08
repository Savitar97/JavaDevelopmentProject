package com.epam.training.ticketservice.core.mapper;

import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;

public interface RoomEntityToDtoMapper {
    RoomDto convertEntityToDto(Room room);
}
