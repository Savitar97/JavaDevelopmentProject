package com.epam.training.ticketservice.core.screening.persistence.repository;

import com.epam.training.ticketservice.core.screening.persistence.entity.ScreeningId;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, ScreeningId> {
    boolean existsById_Movie_TitleAndId_Room_NameAndId_StartTime(String id_movie_title, String id_room_name, Date id_startTime);

    Screening getScreeningById_Movie_TitleAndId_Room_NameAndId_StartTime(String id_movie_title, String id_room_name, Date id_startTime);

    List<Screening> getAllByIdRoomNameEquals(String roomName);
}
