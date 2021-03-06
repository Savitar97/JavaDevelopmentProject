package com.epam.training.ticketservice.core.room;

import com.epam.training.ticketservice.core.pricecomponent.persistence.entity.PriceComponent;
import com.epam.training.ticketservice.core.room.model.RoomDto;

import java.util.List;

public interface RoomService {
    List<RoomDto> getRoomList();

    void createRoom(RoomDto roomDto);

    void updateRoom(RoomDto roomDto);

    void deleteRoomByName(String name);

    boolean existsByName(String name);

    void updatePriceComponent(PriceComponent priceComponent, String roomName);
}
