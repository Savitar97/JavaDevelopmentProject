package com.epam.training.ticketservice.ui.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class UserCommand extends CommandAvailability {

    @ShellMethod(value = "Login as Admin", key = "sign in privileged")
    public String loginPrivileged(String name, String password) {
        return "Login failed due to incorrect credentials";
    }

    @ShellMethod(value = "Login as User", key = "sign in")
    public String login() {
        return "Login failed due to incorrect credentials";
    }

    @ShellMethod(value = "Logout", key = "sign out")
    public String logout() {
        return "You are signed out!";
    }

    @ShellMethod(value = "Logout", key = "describe account")
    public String describeAccount() {
        return "You are signed out!";
    }
}
