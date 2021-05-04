package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.SeatDto;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Date;
import java.util.List;

@ShellComponent
public class BookingCommand extends CommandAvailability {
    private final BookingService bookingService;

    public BookingCommand(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @ShellMethod(value = "Book for a screening", key = "book")
    public String booking(String movieTitle, String roomName, Date startTime, List<SeatDto> seats) {
        try{
            bookingService.createBooking(movieTitle, roomName, startTime, seats);
            return "Booking success";
        } catch (Exception e){
            return e.getMessage();
        }

    }
}
