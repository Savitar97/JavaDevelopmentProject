package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.ui.utilities.out.helper.ConvertListToString;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

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

    @ShellMethod(value = "Store a screening", key = "create screening")
    public void createScreening(String movieTitle, String roomName, Date date) {
        try {
            screeningService.createScreening(movieTitle, roomName, date);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @ShellMethod(value = "List the available screenings",key = "list screenings")
    public String getScreeningList() {
        List<ScreeningDto> screenings = screeningService.getScreening();
        if (screenings.isEmpty()) {
            return "There are no screenings";
        }
        return convertListToString.listToString(screenings);
    }
}
