package com.epam.training.ticketservice.core.screening.impl;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.persistence.entity.ScreeningId;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class ScreeningServiceImpl implements ScreeningService {

    private final RoomRepository roomRepository;

    private final MovieRepository movieRepository;

    private final ScreeningRepository screeningRepository;

    ScreeningServiceImpl(RoomRepository roomRepository,
                         MovieRepository movieRepository,
                         ScreeningRepository screeningRepository) {
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
    }

    @Override
    public List<Screening> getScreening() {
        return null;
    }

    @Override
    public void createScreening(String movieTitle, String roomName, Date starTime) {
        Movie movie = movieRepository.getMovieByTitle(movieTitle);
        Room room = roomRepository.findByName(roomName);
        ScreeningId screeningId = new ScreeningId(movie,room,starTime);
        screeningRepository.save(new Screening(screeningId));
    }
}
