package com.epam.training.ticketservice.core.mapper.impl;

import com.epam.training.ticketservice.core.mapper.UserEntityToDtoMapper;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserEntityToDtoMapperImpl implements UserEntityToDtoMapper {
    @Override
    public UserDto convertEntityToDto(User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
