package com.epam.training.ticketservice.core.movie.persistence.repository;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Integer> {
    Movie findByTitle(String title);
    @Transactional
    void deleteByTitle(String title);
}
