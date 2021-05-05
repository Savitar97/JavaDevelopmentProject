package com.epam.training.ticketservice.core.room.persistence.repository;

import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Room getRoomByName(String name);

    Optional<Room> findByName(String name);

    @Transactional
    void deleteByName(String name);

    boolean existsByName(String name);
}
