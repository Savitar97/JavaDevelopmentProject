package com.epam.training.ticketservice.core.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class RegistrationUserDto {
    private final String username;
    private final String password;
}
