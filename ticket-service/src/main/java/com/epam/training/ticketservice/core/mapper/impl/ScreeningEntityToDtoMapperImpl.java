package com.epam.training.ticketservice.core.mapper.impl;

import com.epam.training.ticketservice.core.mapper.MovieEntityToDtoMapper;
import com.epam.training.ticketservice.core.mapper.RoomEntityToDtoMapper;
import com.epam.training.ticketservice.core.mapper.ScreeningEntityToDtoMapper;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import org.springframework.stereotype.Component;

@Component
public class ScreeningEntityToDtoMapperImpl
        implements ScreeningEntityToDtoMapper {
    private final MovieEntityToDtoMapper movieEntityToDtoMapper;
    private final RoomEntityToDtoMapper roomEntityToDtoMapper;

    public ScreeningEntityToDtoMapperImpl(MovieEntityToDtoMapper movieEntityToDtoMapper,
                                          RoomEntityToDtoMapper roomEntityToDtoMapper) {
        this.movieEntityToDtoMapper = movieEntityToDtoMapper;
        this.roomEntityToDtoMapper = roomEntityToDtoMapper;
    }

    public ScreeningDto convertEntityToDto(Screening screening) {
        return ScreeningDto.builder()
                .movie(movieEntityToDtoMapper
                        .convertEntityToDto(screening.getId().getMovie()))
                .room(roomEntityToDtoMapper
                        .convertEntityToDto(screening.getId().getRoom()))
                .startTime(screening.getId().getStartTime())
                .build();
    }
}
