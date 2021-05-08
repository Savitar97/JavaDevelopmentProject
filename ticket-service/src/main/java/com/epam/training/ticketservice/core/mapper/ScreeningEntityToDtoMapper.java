package com.epam.training.ticketservice.core.mapper;

import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;

public interface ScreeningEntityToDtoMapper {
    ScreeningDto convertEntityToDto(Screening screening);

}
