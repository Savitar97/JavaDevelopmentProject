package com.epam.training.ticketservice.ui.commands;

import org.springframework.shell.Availability;

public abstract class CommandAvailability {

    public Availability isUserSignedIn() {
        return  Availability.unavailable("Command just available for ROLE_ADMIN.");
    }
}
