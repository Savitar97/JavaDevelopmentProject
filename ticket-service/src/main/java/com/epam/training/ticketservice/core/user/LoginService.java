package com.epam.training.ticketservice.core.user;

public interface LoginService {
    void signIn(String username, String password);

    void signOut();
}
