package com.epam.training.ticketservice.core.user;

import com.epam.training.ticketservice.core.user.model.RegistrationUserDto;

public interface UserService {

    void registerUser(RegistrationUserDto registrationUser);

    String describeAccount();
}
