package com.epam.training.ticketservice.core.user;

public interface LoginService {
    String signIn(String username, String password);

    void signOut();
}
