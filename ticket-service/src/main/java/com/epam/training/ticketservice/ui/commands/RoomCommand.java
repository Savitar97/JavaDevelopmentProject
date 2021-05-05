package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.ui.utilities.out.helper.ConvertListToString;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;

@ShellComponent
public class RoomCommand extends CommandAvailability {

    private final RoomService roomService;
    private final ConvertListToString convertListToString;

    public RoomCommand(RoomService roomService, ConvertListToString convertListToString) {
        this.roomService = roomService;
        this.convertListToString = convertListToString;
    }

    @ShellMethodAvailability(value = "isUserAdmin")
    @ShellMethod(value = "Add a room to rooms", key = "create room")
    public String createRoom(String name, Integer rows, Integer columns) {
        RoomDto roomDto = new RoomDto.Builder()
                .withRoomName(name)
                .withSeatRows(rows)
                .withSeatColumns(columns)
                .build();
        try {
            roomService.createRoom(roomDto);
            return roomDto.toString();
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "List the available rooms", key = "list rooms")
    public String getRoomList() {
        List<RoomDto> rooms = roomService.getRoomList();
        if (rooms == null || rooms.isEmpty()) {
            return "There are no rooms at the moment";
        }
        return convertListToString.listToString(rooms);
    }

    @ShellMethodAvailability(value = "isUserAdmin")
    @ShellMethod(value = "Delete a room from rooms", key = "delete room")
    public String deleteRoom(String name) {
        try {
            roomService.deleteRoomByName(name);
            return "Delete was successful";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @ShellMethodAvailability(value = "isUserAdmin")
    @ShellMethod(value = "Update a room from rooms", key = "update room")
    public String updateRoom(String name, Integer rows, Integer columns) {
        RoomDto roomDto = new RoomDto.Builder()
                .withRoomName(name)
                .withSeatRows(rows)
                .withSeatColumns(columns)
                .build();
        try {
            roomService.updateRoom(roomDto);
            return "Update was successful";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

}
