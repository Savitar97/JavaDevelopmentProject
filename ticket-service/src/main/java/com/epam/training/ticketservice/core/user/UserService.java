package com.epam.training.ticketservice.core.user;

import com.epam.training.ticketservice.core.user.model.RegistrationUserDto;
import com.epam.training.ticketservice.core.user.model.UserDto;

public interface UserService {

    void registerUser(RegistrationUserDto registrationUser);

    UserDto describeAccount();
}
