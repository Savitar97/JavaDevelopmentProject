package com.epam.training.ticketservice.core.booking.impl;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.model.SeatDto;
import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.entity.Seat;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.mapper.BookingEntityToDtoMapper;
import com.epam.training.ticketservice.core.pricecomponent.persistence.repository.BasePriceRepository;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
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
    private final BookingEntityToDtoMapper entityToDtoMapper;
    private final BasePriceRepository basePriceRepository;


    public BookingServiceImpl(ScreeningRepository screeningRepository,
                              BookingRepository bookingRepository,
                              UserRepository userRepository,
                              BookingEntityToDtoMapper entityToDtoMapper, BasePriceRepository basePriceRepository) {
        this.screeningRepository = screeningRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.entityToDtoMapper = entityToDtoMapper;
        this.basePriceRepository = basePriceRepository;
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
                                .getName()).orElseThrow(() -> new IllegalStateException("You are not signed in")),
                screeningRepository
                        .getScreeningById_Movie_TitleAndId_Room_NameAndId_StartTime(movieTitle,
                                roomName,
                                startTime),
                seatEntities,
                calculatePrice(movieTitle, roomName, startTime, seats)
        );
        checkSeatExisting(booking);
        checkSeatAlreadyBooked(booking);
        bookingRepository.save(booking);
        return booking.toString();
    }

    @Override
    public List<BookingDto> getBookingForUser(String userName) {
        return bookingRepository.getBookingByUser_Username(userName).stream()
                .map(entityToDtoMapper::convertEntityToDto)
                .collect(Collectors.toUnmodifiableList());
    }

    private void checkSeatExisting(Booking booking) {
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

        booking.getSeats().forEach(seat -> {
            if (seatRows < 0 || seatColumns < 0
                    || seatRows < seat.getSeatRow() || seatColumns < seat.getSeatColumn()) {
                throw new IllegalArgumentException("Seat " + seat + " does not exist in this room");
            }
        });
    }

    private void checkSeatAlreadyBooked(Booking booking) {
        List<Seat> seats = bookingRepository
                .getBookingByScreening(booking.getScreening())
                .stream()
                .map(Booking::getSeats)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        booking.getSeats().forEach(seat -> {
            if (seats.contains(seat)) {
                throw new IllegalArgumentException("Seat " + seat + " is already taken");
            }
        });
    }

    public Integer calculatePrice(String movieTitle, String roomName, Date startTime, List<SeatDto> seats) {
        Screening screening = screeningRepository
                .findById_Movie_TitleAndId_Room_NameAndId_StartTime(movieTitle, roomName, startTime)
                .orElseThrow(() -> new IllegalArgumentException("Screening with this parameter not exist!"));

        Integer moviePrice;
        Integer roomPrice;
        Integer screeningPrice;

        try {
            moviePrice = screening.getId().getMovie().getPriceComponent().getPrice();
        } catch (NullPointerException e) {
            moviePrice = 0;
        }

        try {
            roomPrice = screening.getId().getRoom().getPriceComponent().getPrice();
        } catch (NullPointerException e) {
            roomPrice = 0;
        }

        try {
            screeningPrice = screening.getPriceComponent().getPrice();
        } catch (NullPointerException e) {
            screeningPrice = 0;
        }
        Integer basePrice = basePriceRepository.getBasePriceById(1).getPrice();
        return seats.size() * (moviePrice + roomPrice + screeningPrice + basePrice);
    }
}
