package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.booking.model.SeatDto;
import com.epam.training.ticketservice.core.pricecomponent.PriceComponentService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Date;
import java.util.List;

@ShellComponent
public class PriceComponentCommand extends CommandAvailability {

    private final PriceComponentService priceComponentService;

    public PriceComponentCommand(PriceComponentService priceComponentService) {
        this.priceComponentService = priceComponentService;
    }

    @ShellMethodAvailability(value = "isUserAdmin")
    @ShellMethod(value = "create a price component", key = "create price component")
    public String createPriceComponent(String name, Integer price) {
        try {
            priceComponentService.createPriceComponent(name, price);
            return "Create was successful";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @ShellMethodAvailability(value = "isUserAdmin")
    @ShellMethod(value = "Attach price component to a movie", key = "attach price component to movie")
    public String attachPriceComponentToMovie(String priceComponentName, String movieTitle) {
        try {
            priceComponentService.attachPriceComponentToMovie(priceComponentName, movieTitle);
            return "Attach was successful";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @ShellMethodAvailability(value = "isUserAdmin")
    @ShellMethod(value = "Attach price component to a room", key = "attach price component to room")
    public String attachPriceComponentToRoom(String priceComponentName, String roomName) {
        try {
            priceComponentService.attachPriceComponentToRoom(priceComponentName, roomName);
            return "Attach was successful";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @ShellMethodAvailability(value = "isUserAdmin")
    @ShellMethod(value = "Attach price component to a screening", key = "attach price component to screening")
    public String attachPriceComponentToScreening(String priceComponentName,String movieTitle,
                                                  String roomName, Date startTime
                                                  ) {
        try {
            priceComponentService.attachPriceComponentToScreening(priceComponentName, movieTitle, roomName, startTime);
            return "Attach was successful";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Show price for a screening", key = "show price for")
    public String showPrice(String movieTitle, String roomName, Date startTime, List<SeatDto> seats) {
        try {
            Integer price = priceComponentService.showPriceForScreening(movieTitle, roomName, startTime, seats);
            return "The price for this booking would be " + price + " HUF";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @ShellMethodAvailability(value = "isUserAdmin")
    @ShellMethod(value = "Update base price", key = "update base price")
    public void setBasePrice(Integer price) {
        priceComponentService.updateBasePrice(price);
    }
}
