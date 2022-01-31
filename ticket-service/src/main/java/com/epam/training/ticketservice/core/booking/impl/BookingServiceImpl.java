package com.epam.training.ticketservice.core.booking.impl;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.model.SeatDto;
import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.entity.Seat;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.mapper.BookingEntityToDtoMapper;
import com.epam.training.ticketservice.core.pricecomponent.persistence.entity.PriceComponent;
import com.epam.training.ticketservice.core.pricecomponent.persistence.repository.BasePriceRepository;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import com.epam.training.ticketservice.core.writer.OutputStringWriter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private final ScreeningRepository screeningRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookingEntityToDtoMapper entityToDtoMapper;
    private final BasePriceRepository basePriceRepository;
    private final OutputStringWriter<Booking> outputStringWriter;
    private final OutputStringWriter<Seat> outputSeatWriter;

    public BookingServiceImpl(ScreeningRepository screeningRepository,
                              BookingRepository bookingRepository,
                              UserRepository userRepository,
                              BookingEntityToDtoMapper entityToDtoMapper,
                              BasePriceRepository basePriceRepository,
                              OutputStringWriter<Booking> outputStringWriter,
                              OutputStringWriter<Seat> outputSeatWriter) {
        this.screeningRepository = screeningRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.entityToDtoMapper = entityToDtoMapper;
        this.basePriceRepository = basePriceRepository;
        this.outputStringWriter = outputStringWriter;
        this.outputSeatWriter = outputSeatWriter;
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

        User user = userRepository.findByUsername(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName()).orElseThrow(() -> new IllegalStateException("You are not signed in"));

        Screening screening = screeningRepository
                .getScreeningById_Movie_TitleAndId_Room_NameAndId_StartTime(movieTitle, roomName, startTime);

        Integer ticketPrice = calculatePrice(movieTitle, roomName, startTime, seats);
        Booking booking = Booking.builder()
                .user(user)
                .screening(screening)
                .seats(seatEntities)
                .ticketPrice(ticketPrice).build();

        checkSeatExisting(booking);
        checkSeatAlreadyBooked(booking);
        bookingRepository.save(booking);
        return outputStringWriter.writeOutAsString(booking);
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

        booking.getSeats().forEach(seat ->
            checkSeatExistInRoom(seatColumns, seatRows, seat)
        );
    }

    private void checkSeatExistInRoom(Integer seatColumns, Integer seatRows, Seat seat) {
        if (seatRows < 0 || seatColumns < 0
                || seatRows < seat.getSeatRow() || seatColumns < seat.getSeatColumn()) {
            throw new IllegalArgumentException("Seat "
                    + outputSeatWriter.writeOutAsString(seat)
                    + " does not exist in this room");
        }
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
                throw new IllegalArgumentException("Seat "
                        + outputSeatWriter.writeOutAsString(seat)
                        + " is already taken");
            }
        });
    }

    public Integer calculatePrice(String movieTitle, String roomName, Date startTime, List<SeatDto> seats) {
        Screening screening = screeningRepository
                .findById_Movie_TitleAndId_Room_NameAndId_StartTime(movieTitle, roomName, startTime)
                .orElseThrow(() -> new IllegalArgumentException("Screening with this parameter not exist!"));

        int moviePrice = Optional.ofNullable(screening
                .getId()
                .getMovie()
                .getPriceComponent())
                .map(PriceComponent::getPrice)
                .orElse(0);
        int roomPrice = Optional.ofNullable(screening
                .getId().getRoom()
                .getPriceComponent())
                .map(PriceComponent::getPrice)
                .orElse(0);
        int screeningPrice = Optional.ofNullable(screening
                .getPriceComponent())
                .map(PriceComponent::getPrice)
                .orElse(0);

        Integer basePrice = basePriceRepository.getBasePriceById(1).getPrice();
        return seats.size() * (moviePrice + roomPrice + screeningPrice + basePrice);
    }


}
