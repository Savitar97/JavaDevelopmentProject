package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.pricecomponent.persistence.entity.PriceComponent;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;

import java.util.Date;
import java.util.List;


public interface ScreeningService {
    List<ScreeningDto> getScreening();

    void createScreening(String movieTitle,String roomName, Date startTime);

    void deleteScreening(String movieTitle,String roomName, Date startTime);

    void updatePriceComponent(PriceComponent priceComponent, String movieTitle,String roomName, Date startTime);
}
