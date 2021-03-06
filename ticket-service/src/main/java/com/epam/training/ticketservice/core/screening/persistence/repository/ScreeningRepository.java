package com.epam.training.ticketservice.core.screening.persistence.repository;

import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.entity.ScreeningId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, ScreeningId> {

    boolean existsById_Movie_TitleAndId_Room_NameAndId_StartTime(String movieTitle,
                                                                 String roomName,
                                                                 Date startTime);

    Screening getScreeningById_Movie_TitleAndId_Room_NameAndId_StartTime(String movieTitle,
                                                                         String roomName,
                                                                         Date startTime);

    @Transactional
    void deleteScreeningById_Movie_TitleAndId_Room_NameAndId_StartTime(String movieTitle,
                                                                       String roomName,
                                                                       Date startTime);

    List<Screening> getAllByIdRoomNameEquals(String roomName);

    Optional<Screening> findById_Movie_TitleAndId_Room_NameAndId_StartTime(String movieTitle,
                                                                           String roomName,
                                                                           Date startTime);
}
