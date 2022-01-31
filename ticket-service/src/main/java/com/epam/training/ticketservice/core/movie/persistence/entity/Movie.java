package com.epam.training.ticketservice.core.movie.persistence.entity;

import com.epam.training.ticketservice.core.pricecomponent.persistence.entity.PriceComponent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String title;

    private String genre;

    private Integer length;

    @ManyToOne
    @JoinColumn(name = "price_id", referencedColumnName = "id")
    private PriceComponent priceComponent;

    @Override
    public String toString() {
        return "Movie{"
                + "id=" + id
                + ", title='" + title + '\''
                + ", genre='" + genre + '\''
                + ", length=" + length
                + ", priceComponent=" + priceComponent
                + '}';
    }
}
