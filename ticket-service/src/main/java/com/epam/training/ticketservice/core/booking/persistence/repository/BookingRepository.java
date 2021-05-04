package com.epam.training.ticketservice.core.booking.persistence.repository;

import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.entity.Seat;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> getBookingByScreening(Screening screening);
}
