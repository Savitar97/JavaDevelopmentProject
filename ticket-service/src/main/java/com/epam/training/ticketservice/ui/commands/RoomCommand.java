package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;

@ShellComponent
public class RoomCommand {

    private final RoomService roomService;

    public RoomCommand(RoomService roomService) {
        this.roomService = roomService;
    }

    @ShellMethod(value = "Add a room to rooms",key = "create room")
    public RoomDto createRoom(String name,Integer rows,Integer columns){
        RoomDto roomDto=new RoomDto.Builder()
                .withRoomName(name)
                .withSeatRows(rows)
                .withSeatColumns(columns)
                .build();

        roomService.createRoom(roomDto);
        return roomDto;
    }

    @ShellMethod(value = "List the available rooms",key = "list rooms")
    public List<RoomDto> getRoomList(){
        List<RoomDto> rooms = roomService.getRoomList();
        if (rooms == null || rooms.isEmpty()) {
            System.out.println("There are no rooms at the moment");
        }
        return rooms;
    }

    @ShellMethod(value = "Delete a room from rooms",key = "delete room")
    public void deleteRoom(String name){
        roomService.deleteRoomByName(name);
    }

}
