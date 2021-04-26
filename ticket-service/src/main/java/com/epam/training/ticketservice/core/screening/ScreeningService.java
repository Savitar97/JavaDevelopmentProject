package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


public interface ScreeningService{
    List<Screening> getScreening();
    void createScreening(String movieTitle,String roomName, Date starTime);
}
