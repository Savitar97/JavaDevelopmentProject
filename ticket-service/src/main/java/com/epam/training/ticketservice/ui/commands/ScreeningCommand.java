package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.ui.utilities.out.helper.ConvertListToString;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Date;
import java.util.List;

@ShellComponent
public class ScreeningCommand extends CommandAvailability {
    private final ScreeningService screeningService;
    private final ConvertListToString convertListToString;

    public ScreeningCommand(ScreeningService screeningService, ConvertListToString convertListToString) {
        this.screeningService = screeningService;
        this.convertListToString = convertListToString;
    }

    @ShellMethodAvailability(value = "isUserAdmin")
    @ShellMethod(value = "Store a screening", key = "create screening")
    public String createScreening(String movieTitle, String roomName, Date date) {
        try {
            return screeningService.createScreening(movieTitle, roomName, date);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "List the available screenings", key = "list screenings")
    public String getScreeningList() {
        List<ScreeningDto> screenings = screeningService.getScreening();
        if (screenings.isEmpty()) {
            return "There are no screenings";
        }
        return convertListToString.listToString(screenings);
    }

    @ShellMethodAvailability(value = "isUserAdmin")
    @ShellMethod(value = "Delete a screening", key = "delete screening")
    public String deleteScreening(String movieTitle, String roomName, Date date) {
        try {
            screeningService.deleteScreening(movieTitle, roomName, date);
            return "Delete was successful";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }

    }
}
