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

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private final ScreeningRepository screeningRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public BookingServiceImpl(ScreeningRepository screeningRepository, BookingRepository bookingRepository, UserRepository userRepository) {
        this.screeningRepository = screeningRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createBooking(String movieTitle, String roomName, Date startTime, List<SeatDto> seats) {
        if(!screeningRepository
                .existsById_Movie_TitleAndId_Room_NameAndId_StartTime(movieTitle,
                roomName,
                startTime)){
            throw new IllegalArgumentException("Screening not exist");
        }
        List<Seat> seatEntities = seats.stream()
                .map(seat -> new Seat(seat.getRow(), seat.getColumn()))
                .collect(Collectors.toList());

        Booking booking = new Booking(null,
                userRepository.findByUsername(
                        SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName()).orElseThrow(IllegalStateException::new)
                ,
                screeningRepository
                .getScreeningById_Movie_TitleAndId_Room_NameAndId_StartTime(movieTitle,
                        roomName,
                        startTime),seatEntities);
        bookingRepository.save(booking);

    }
}
