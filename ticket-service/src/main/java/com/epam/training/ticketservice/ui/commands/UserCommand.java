package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.user.LoginService;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.RegistrationUserDto;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
public class UserCommand extends CommandAvailability {

    private final UserService userService;

    private final LoginService loginService;


    public UserCommand(UserService userService, LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }

    @ShellMethod(value = "Login as Admin", key = "sign in privileged")
    public void loginPrivileged(String name, String password) {
        loginService.login(name,password);
    }

    @ShellMethod(value = "Register as User", key = "sign up")
    public String register(String name, String password) {
        try {
            userService.registerUser(new RegistrationUserDto(name,password));
            return "Registration was successful";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Login as User", key = "sign in")
    public void login(String name, String password) {
        loginService.login(name,password);
    }

    @ShellMethodAvailability(value = "isUserSignedIn")
    @ShellMethod(value = "Logout", key = "sign out")
    public String logout() {
        loginService.signOut();
        return "You are signed out!";
    }

    @ShellMethod(value = "Logout", key = "describe account")
    public String describeAccount() {
        return userService.describeAccount();
    }


}
