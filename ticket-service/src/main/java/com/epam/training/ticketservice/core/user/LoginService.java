package com.epam.training.ticketservice.core.user;

public interface LoginService {
    void login(String username, String password);

    void signOut();
}
