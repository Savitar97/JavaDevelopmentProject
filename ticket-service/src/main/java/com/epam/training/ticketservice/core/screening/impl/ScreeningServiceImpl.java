package com.epam.training.ticketservice.core.screening.impl;

import com.epam.training.ticketservice.core.mapper.EntityToDtoMapper;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.ScreeningId;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ScreeningServiceImpl implements ScreeningService {

    private final RoomRepository roomRepository;

    private final MovieRepository movieRepository;

    private final ScreeningRepository screeningRepository;

    private final EntityToDtoMapper entityToDtoMapper;

    ScreeningServiceImpl(RoomRepository roomRepository,
                         MovieRepository movieRepository,
                         ScreeningRepository screeningRepository,
                         EntityToDtoMapper entityToDtoMapper) {
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
        this.entityToDtoMapper = entityToDtoMapper;
    }

    @Override
    public List<ScreeningDto> getScreening() {
        return screeningRepository.findAll().stream()
                .map(entityToDtoMapper::convertEntityToDto)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public String createScreening(String movieTitle, String roomName, Date startTime) {
        Objects.requireNonNull(movieTitle, "Movie title cannot be null");
        Objects.requireNonNull(roomName, "Room name cannot be null");
        Objects.requireNonNull(startTime, "Start time cannot be null");
        if (!movieRepository.existsByTitle(movieTitle)) {
            throw new IllegalArgumentException("Movie with this title not exist");
        }
        if (!roomRepository.existsByName(roomName)) {
            throw new IllegalArgumentException("Room with this name not exist");
        }
        if (screeningRepository.existsById_Movie_TitleAndId_Room_NameAndId_StartTime(
                movieTitle, roomName, startTime)) {
            throw new IllegalArgumentException("Screening already exist");
        }
        Movie movie = movieRepository.getMovieByTitle(movieTitle);
        Room room = roomRepository.getRoomByName(roomName);
        checkOverlapping(room.getName(), startTime, movie.getLength());
        ScreeningId screeningId = new ScreeningId(movie, room, startTime);
        Screening screening = new Screening(screeningId);
        screeningRepository.save(screening);
        return screening.toString();

    }

    @Override
    public void deleteScreening(String movieTitle, String roomName, Date startTime) {
        Objects.requireNonNull(movieTitle, "Movie title cannot be null");
        Objects.requireNonNull(roomName, "Room name cannot be null");
        Objects.requireNonNull(startTime, "Start time cannot be null");

        if (!screeningRepository.existsById_Movie_TitleAndId_Room_NameAndId_StartTime(
                movieTitle, roomName, startTime)) {
            throw new IllegalArgumentException("Screening not exist");
        }

        screeningRepository.deleteScreeningById_Movie_TitleAndId_Room_NameAndId_StartTime(
                movieTitle, roomName, startTime);
    }

    public void checkOverlapping(String roomName, Date desiredDate, Integer movieLength) {
        List<Screening> screenings = screeningRepository.getAllByIdRoomNameEquals(roomName);
        Date desiredDateEnd = DateUtils.addMinutes(desiredDate, movieLength + 10);
        if (!screenings.isEmpty()) {
            for (Screening screening : screenings) {
                Date screeningStartTime = screening.getId().getStartTime();
                Date screeningEndTime = DateUtils.addMinutes(screening.getId().getStartTime(),
                        screening.getId().getMovie().getLength());
                Date screeningEndTimeWithBreak = DateUtils.addMinutes(screeningEndTime, 10);
                if (isOverlapping(screeningStartTime, screeningEndTime,
                        desiredDate, desiredDateEnd)) {
                    throw new IllegalArgumentException("There is an overlapping screening");
                }
                if (isOverlapping(screeningEndTime, screeningEndTimeWithBreak,
                        desiredDate, desiredDateEnd)) {
                    throw new IllegalArgumentException(
                            "This would start in the break period after another screening in this room");
                }

            }
        }

    }

    public boolean isOverlapping(Date startDate, Date endDate, Date desiredDate, Date endDesiredDate) {
        return endDate.after(desiredDate) && startDate.before(endDesiredDate);
    }


}
