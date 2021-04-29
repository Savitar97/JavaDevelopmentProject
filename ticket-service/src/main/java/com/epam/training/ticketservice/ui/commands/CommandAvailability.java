package com.epam.training.ticketservice.ui.commands;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.shell.Availability;

public abstract class CommandAvailability {

    public Availability isUserSignedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication instanceof UsernamePasswordAuthenticationToken)) {
            return Availability.unavailable("You are not signed in");
        }
        return Availability.available();
    }

    public Availability isUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication instanceof  UsernamePasswordAuthenticationToken)) {
            return Availability.unavailable("You are not signed in");
        }
        if (!authentication.getAuthorities().contains("ROLE_ADMIN")) {
            return Availability.unavailable("Permission denied");
        }
        return Availability.available();
    }
}
