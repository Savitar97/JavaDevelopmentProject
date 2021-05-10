package com.epam.training.ticketservice.core.pricecomponent.impl;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.SeatDto;
import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.pricecomponent.PriceComponentService;
import com.epam.training.ticketservice.core.pricecomponent.persistence.entity.BasePrice;
import com.epam.training.ticketservice.core.pricecomponent.persistence.entity.PriceComponent;
import com.epam.training.ticketservice.core.pricecomponent.persistence.repository.BasePriceRepository;
import com.epam.training.ticketservice.core.pricecomponent.persistence.repository.PriceComponentRepository;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PriceComponentServiceImpl implements PriceComponentService {

    private final PriceComponentRepository priceComponentRepository;
    private final MovieService movieService;
    private final RoomService roomService;
    private final ScreeningService screeningService;
    private final BookingService bookingService;
    private final BasePriceRepository basePriceRepository;


    public PriceComponentServiceImpl(PriceComponentRepository priceComponentRepository,
                                     MovieService movieService,
                                     RoomService roomService,
                                     ScreeningService screeningService,
                                     BookingService bookingService,
                                     BasePriceRepository basePriceRepository) {
        this.priceComponentRepository = priceComponentRepository;
        this.movieService = movieService;
        this.roomService = roomService;
        this.screeningService = screeningService;
        this.bookingService = bookingService;
        this.basePriceRepository = basePriceRepository;
    }

    @Override
    public void createPriceComponent(String name, Integer price) {
        if (priceComponentRepository.existsPriceComponentByName(name)) {
            throw new IllegalArgumentException("Price component with this name already exist");
        }
        PriceComponent priceComponent = new PriceComponent(null, name, price);
        priceComponentRepository.save(priceComponent);
    }

    @Override
    public void attachPriceComponentToMovie(String componentName, String title) {
        PriceComponent priceComponent = priceComponentRepository
                .findPriceComponentByName(componentName)
                .orElseThrow(() -> new IllegalArgumentException("Component with this name not exist!"));
        movieService.updatePriceComponent(priceComponent, title);
    }

    @Override
    public void attachPriceComponentToRoom(String componentName, String roomName) {
        PriceComponent priceComponent = priceComponentRepository
                .findPriceComponentByName(componentName)
                .orElseThrow(() -> new IllegalArgumentException("Component with this name not exist!"));
        roomService.updatePriceComponent(priceComponent, roomName);
    }

    @Override
    public void attachPriceComponentToScreening(String componentName, String title, String roomName, Date startDate) {
        PriceComponent priceComponent = priceComponentRepository
                .findPriceComponentByName(componentName)
                .orElseThrow(() -> new IllegalArgumentException("Component with this name not exist!"));
        screeningService.updatePriceComponent(priceComponent, title, roomName, startDate);
    }

    @Override
    public Integer showPriceForScreening(String title, String roomName, Date startDate, List<SeatDto> seats) {
        return bookingService.calculatePrice(title, roomName, startDate, seats);
    }

    @Override
    public void updateBasePrice(Integer price) {
        BasePrice basePrice = basePriceRepository.getBasePriceById(1);
        basePrice.setPrice(price);
        basePriceRepository.save(basePrice);
    }


}
