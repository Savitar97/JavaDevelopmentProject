package com.epam.training.ticketservice.core.screening.model;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class ScreeningDto {
    private final MovieDto movie;
    private final RoomDto room;
    private final Date startTime;

    @Override
    public String toString() {
        return movie + ", screened in room "
                + room.getRoomName() + ", at"
                + startTime;
    }
}
