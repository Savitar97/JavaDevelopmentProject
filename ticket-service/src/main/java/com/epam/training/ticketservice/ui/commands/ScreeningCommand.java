package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.screening.ScreeningService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@ShellComponent
public class ScreeningCommand {
    private final ScreeningService screeningService;
    protected static final String DATE_FORMAT = "yyyy-mm-dd HH:mm";

    public ScreeningCommand(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    @ShellMethod(value="Store a screening",key="create screening")
    public void createScreening(String movieTitle, String roomName, String dateString) {
        Date date;
        try {
            date = new SimpleDateFormat(DATE_FORMAT).parse(dateString);
        } catch (IllegalArgumentException | ParseException e) {
            System.out.println("Invalid date format");
            return;
        }

        try {
            screeningService.createScreening(movieTitle, roomName, date);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
