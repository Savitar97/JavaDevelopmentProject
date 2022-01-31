package com.epam.training.ticketservice.core.booking.persistence.entity;

import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Screening screening;

    @ElementCollection(fetch = FetchType.EAGER)
    List<Seat> seats;

    private Integer ticketPrice;

    @Override
    public String toString() {
        return "Booking{"
                + "id=" + id
                + ", user=" + user
                + ", screening=" + screening
                + ", seats=" + seats
                + ", ticketPrice=" + ticketPrice
                + '}';
    }
}
