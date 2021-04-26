package com.epam.training.ticketservice.core.screening.persistence.entity;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Embeddable
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ScreeningId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="movie_id",referencedColumnName = "id")
    Movie movie;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_id",referencedColumnName = "id")
    Room room;
    Date startTime;

}