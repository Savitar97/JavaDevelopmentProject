package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.ui.utilities.out.helper.ConvertListToString;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;

@ShellComponent
public class RoomCommand extends CommandAvailability {

    private final RoomService roomService;
    private final ConvertListToString convertListToString;

    public RoomCommand(RoomService roomService, ConvertListToString convertListToString) {
        this.roomService = roomService;
        this.convertListToString = convertListToString;
    }

    @ShellMethod(value = "Add a room to rooms",key = "create room")
    public String createRoom(String name,Integer rows,Integer columns) {
        if (roomService.existsByName(name)) {
            return "Room with this name already exist";
        }
        RoomDto roomDto = new RoomDto.Builder()
                .withRoomName(name)
                .withSeatRows(rows)
                .withSeatColumns(columns)
                .build();

        roomService.createRoom(roomDto);
        return roomDto.toString();
    }

    @ShellMethod(value = "List the available rooms",key = "list rooms")
    public String getRoomList() {
        List<RoomDto> rooms = roomService.getRoomList();
        if (rooms == null || rooms.isEmpty()) {
            return "There are no rooms at the moment";
        }
        return convertListToString.listToString(rooms);
    }

    @ShellMethod(value = "Delete a room from rooms",key = "delete room")
    public void deleteRoom(String name) {
        roomService.deleteRoomByName(name);
    }

}
