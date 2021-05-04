package com.epam.training.ticketservice.core.booking.impl;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.SeatDto;
import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.entity.Seat;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private final ScreeningRepository screeningRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    private final Integer TICKET_PRICE = 1500;

    public BookingServiceImpl(ScreeningRepository screeningRepository,
                              BookingRepository bookingRepository,
                              UserRepository userRepository) {
        this.screeningRepository = screeningRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String createBooking(String movieTitle, String roomName, Date startTime, List<SeatDto> seats) {

        if (!screeningRepository.existsById_Movie_TitleAndId_Room_NameAndId_StartTime(movieTitle,
                        roomName,
                        startTime)) {
            throw new IllegalArgumentException("Screening not exist");
        }
        List<Seat> seatEntities = seats.stream()
                .map(seat -> new Seat(seat.getRow(), seat.getColumn()))
                .collect(Collectors.toList());

        Booking booking = new Booking(null,
                userRepository.findByUsername(
                        SecurityContextHolder.getContext()
                                .getAuthentication()
                                .getName()).orElseThrow(IllegalStateException::new),
                screeningRepository
                        .getScreeningById_Movie_TitleAndId_Room_NameAndId_StartTime(movieTitle,
                                roomName,
                                startTime),
                seatEntities,
                seatEntities.size()*TICKET_PRICE);
        checkSeatExisting(booking);
        checkSeatAlreadyBooked(booking);
        bookingRepository.save(booking);
        return booking.toString();
    }

    public void checkSeatExisting(Booking booking){
        Integer seatColumns = booking
                .getScreening()
                .getId()
                .getRoom()
                .getSeatColumns();

        Integer seatRows = booking
                .getScreening()
                .getId()
                .getRoom()
                .getSeatRows();

        booking.getSeats().forEach(seat->{
            if (seatRows<seat.getSeatRow()||seatColumns<seat.getSeatColumn()){
                throw new IllegalArgumentException("Seat "+seat+" does not exist in this room");
            }
        });
    }

    public void checkSeatAlreadyBooked(Booking booking){
        List<Seat> seats = bookingRepository
                .getBookingByScreening(booking.getScreening())
                .stream()
                .map(Booking::getSeats)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        booking.getSeats().forEach(seat -> {
            if (seats.contains(seat)){
                throw new IllegalArgumentException("Seat "+seat+" is already taken");
            }
        });
    }
}
