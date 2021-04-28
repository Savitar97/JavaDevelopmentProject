package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.screening.ScreeningService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Date;

@ShellComponent
public class ScreeningCommand extends CommandAvailability {
    private final ScreeningService screeningService;

    public ScreeningCommand(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    @ShellMethod(value = "Store a screening", key = "create screening")
    public void createScreening(String movieTitle, String roomName, Date date) {
        try {
            screeningService.createScreening(movieTitle, roomName, date);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
