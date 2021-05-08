package com.epam.training.ticketservice.core.mapper;

import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;

public interface UserEntityToDtoMapper {
    UserDto convertEntityToDto(User user);
}
