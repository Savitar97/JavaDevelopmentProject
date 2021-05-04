package com.epam.training.ticketservice.core.user.impl;

import com.epam.training.ticketservice.core.user.LoginService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;

    public LoginServiceImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Override
    public String signIn(String username, String password) {
        try {
            Authentication result = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(result);
            return "Login success";
        } catch (AuthenticationException e) {
            return "Login failed due to incorrect credentials";
        }
    }

    @Override
    public void signOut() {
        SecurityContextHolder.clearContext();
    }
}
