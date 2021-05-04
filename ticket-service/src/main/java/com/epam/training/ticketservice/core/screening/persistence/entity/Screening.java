package com.epam.training.ticketservice.core.screening.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Screening {
    @EmbeddedId
    ScreeningId id;

    @Override
    public String toString() {
        return "Screening: movie="
                + id.movie.getTitle()
                + " room=" + id.room.getName()
                + " időpont=" + id.getStartTime();
    }
}
