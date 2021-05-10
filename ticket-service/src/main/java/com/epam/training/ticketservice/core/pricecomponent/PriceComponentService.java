package com.epam.training.ticketservice.core.pricecomponent;

import com.epam.training.ticketservice.core.booking.model.SeatDto;

import java.util.Date;
import java.util.List;

public interface PriceComponentService {
    void createPriceComponent(String name,Integer price);

    void attachPriceComponentToMovie(String componentName, String title);

    void attachPriceComponentToRoom(String componentName, String roomName);

    void attachPriceComponentToScreening(String componentName, String title, String roomName, Date startDate);

    Integer showPriceForScreening(String title, String roomName, Date startDate, List<SeatDto> seats);

    void updateBasePrice(Integer price);
}
