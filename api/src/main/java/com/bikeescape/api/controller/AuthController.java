package com.bikeescape.api.controller;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import com.bikeescape.api.converter.UserConverter;
import com.bikeescape.api.exceptions.UserFoundException;
import com.bikeescape.api.exceptions.UserIsRemovedException;
import com.bikeescape.api.model.user.User;
import com.bikeescape.api.model.user.UserLoggedInTO;
import com.bikeescape.api.model.user.UserLoginTO;
import com.bikeescape.api.model.user.UserRegistrationTO;
import com.bikeescape.api.security.AuthService;
import com.bikeescape.api.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Log
@RestController
@CrossOrigin
@RequestMapping("auth")
public class AuthController {

    private final UserService userService;
    private final UserConverter userConverter;
    private final AuthService authService;

    @Autowired
    public AuthController(UserService userService, UserConverter userConverter, AuthService authService) {
        this.userService = userService;
        this.userConverter = userConverter;
        this.authService = authService;
    }

    @GetMapping(path = "/authenticate")
    public void authenticate() {
    }

    @PostMapping(value = "/login")
    @Transactional
    public ResponseEntity<?> login(@RequestBody UserLoginTO userLoginTO, HttpServletResponse response) {
        authService.login(userLoginTO, response);
        User user = userService.findByUsernameOrEmail(userLoginTO.getEmail().toLowerCase());
        if (user.isRemoved()) throw new UserIsRemovedException("User is removed");
        UserLoggedInTO userLoggedInTO = userConverter.convertUserToLoggedInUser(user);
        return new ResponseEntity<>(userLoggedInTO, HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserLoggedInTO signup(@RequestBody UserRegistrationTO userRegistrationTO, HttpServletResponse response) throws MessagingException {
        if (this.userService.emailExists(userRegistrationTO.getEmail())) {
            throw new UserFoundException("User already exist, try another e-mail or reset password");
        }
        User user = this.userService.save(userRegistrationTO);
        log.info("User created: " + userRegistrationTO.toString());
        authService.login(user, response);
        return userConverter.convertUserToLoggedInUser(user);
    }

}