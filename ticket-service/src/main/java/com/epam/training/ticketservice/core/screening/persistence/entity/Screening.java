package com.epam.training.ticketservice.core.screening.persistence.entity;

import com.epam.training.ticketservice.core.pricecomponent.persistence.entity.PriceComponent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EmbeddedId;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Screening {
    @EmbeddedId
    ScreeningId id;

    @ManyToOne
    @JoinColumn(name = "price_id", referencedColumnName = "id")
    private PriceComponent priceComponent;

    @Override
    public String toString() {
        return "Screening: movie="
                + id.movie.getTitle()
                + " room=" + id.room.getName()
                + " időpont=" + id.getStartTime();
    }
}
