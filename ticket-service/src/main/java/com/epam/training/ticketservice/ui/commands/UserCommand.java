package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.user.LoginService;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.RegistrationUserDto;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.Role;
import com.epam.training.ticketservice.ui.utilities.out.helper.ConvertListToString;
import org.springframework.security.core.AuthenticationException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;

@ShellComponent
public class UserCommand extends CommandAvailability {

    private final UserService userService;
    private final BookingService bookingService;
    private final LoginService loginService;
    private final ConvertListToString convertListToString;


    public UserCommand(UserService userService, BookingService bookingService,
                       LoginService loginService,
                       ConvertListToString convertListToString) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.loginService = loginService;
        this.convertListToString = convertListToString;
    }

    @ShellMethod(value = "Login as Admin", key = "sign in privileged")
    public String loginPrivileged(String name, String password) {
        try {
            loginService.signIn(name, password);
            return "Login Successful";
        } catch (AuthenticationException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Register as User", key = "sign up")
    public String register(String name, String password) {
        try {
            userService.registerUser(new RegistrationUserDto(name, password));
            return "Registration was successful";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Login as User", key = "sign in")
    public String login(String name, String password) {
        try {
            loginService.signIn(name, password);
            return "Login Successful";
        } catch (AuthenticationException e) {
            return e.getMessage();
        }
    }

    @ShellMethodAvailability(value = "isUserSignedIn")
    @ShellMethod(value = "Logout", key = "sign out")
    public String logout() {
        loginService.signOut();
        return "You are signed out!";
    }

    @ShellMethod(value = "Logout", key = "describe account")
    public String describeAccount() {
        try {
            UserDto userDto = userService.describeAccount();
            if (userDto.getRole().equals(Role.ROLE_ADMIN)) {
                return "Signed in with privileged account '"
                        + userDto.getUsername()
                        + "'";
            } else {
                return createOutputForNormalUser(userDto);
            }
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
    }

    private String createOutputForNormalUser(UserDto userDto) {
        List<BookingDto> bookings = bookingService
                .getBookingForUser(userDto.getUsername());

        StringBuilder sb = new StringBuilder();
        sb.append("Signed in with account '")
                .append(userDto.getUsername())
                .append("'")
                .append(System.lineSeparator());
        if (bookings.isEmpty()) {
            sb.append("You have not booked any tickets yet");
            return sb.toString();
        }
        sb.append("Your previous bookings are")
                .append(System.lineSeparator())
                .append(convertListToString.listToString(bookings));
        return sb.toString();
    }

}
