package com.bikeescape.api.controller;

import com.bikeescape.api.converter.UserConverter;
import com.bikeescape.api.model.user.User;
import com.bikeescape.api.model.user.UserEditTO;
import com.bikeescape.api.model.user.UserEmailTO;
import com.bikeescape.api.model.user.UserResetPasswordTO;
import com.bikeescape.api.security.AuthService;
import com.bikeescape.api.service.UserService;
import com.bikeescape.api.util.CustomMessage;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Log
@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final UserConverter userConverter;

    @Autowired
    public UserController(UserService userService, AuthService authService, UserConverter userConverter) {
        this.userService = userService;
        this.authService = authService;
        this.userConverter = userConverter;
    }

    @ApiOperation(value = "Get logged in user info")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLoggedInUserInfo(HttpServletRequest request) {
        User user = authService.getLoggedUser(request);
        return new ResponseEntity<>(userConverter.convertUserToLoggedInUser(user), HttpStatus.OK);
    }

    @ApiOperation(value = "Send mail with reset password link")
    @RequestMapping(value = "/sendToken", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendResetPasswordLink(@RequestBody UserEmailTO userEmailTO) throws MessagingException {
        userService.createResetToken(userEmailTO.getEmail());
        CustomMessage customMessage = new CustomMessage("Reset password link sent");
        log.info("Password reset create token, user: " + userEmailTO.getEmail());
        return new ResponseEntity<>(customMessage, HttpStatus.OK);
    }

    @ApiOperation(value = "Reset password with token")
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resetPassword(@RequestBody UserResetPasswordTO userResetPasswordTO) {
        User user = userService.changePasswordWithToken(userResetPasswordTO);
        CustomMessage customMessage = new CustomMessage("Password changed");
        log.info("Password reset change password, user: " + user.getEmail());
        return new ResponseEntity<>(customMessage, HttpStatus.OK);
    }

    @ApiOperation(value = "Edit user profile data")
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editUser(@RequestBody UserEditTO userEditTO, HttpServletRequest request) {
        User user = authService.getLoggedUser(request);
        user.setFirstName(userEditTO.getFirstName());
        userService.save(user);
        CustomMessage customMessage = new CustomMessage("User profile changed");
        log.info("User profile changed: " + userEditTO.getFirstName());
        return new ResponseEntity<>(customMessage, HttpStatus.OK);
    }

    @ApiOperation(value = "Mark user account as removed")
    @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> removeUser(HttpServletRequest request) {
        User user = authService.getLoggedUser(request);
        userService.removeUser(user.getId());
        CustomMessage customMessage = new CustomMessage("User removed");
        log.info("User removed: " + user);
        return new ResponseEntity<>(customMessage, HttpStatus.OK);
    }

    //    statistics

    @ApiOperation(value = "Get last n registered users")
    @RequestMapping(value = "registered", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLastRegisteredUsers(@RequestParam(value = "limit", defaultValue = "3") int limit) {
        List<User> users = userService.findLastRegisteredUsers(limit);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @ApiOperation(value = "Get last n removed users")
    @RequestMapping(value = "removed", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLastRemovedUsers(@RequestParam(value = "limit", defaultValue = "3") int limit) {
        List<User> users = userService.findLastRemovedUsers(limit);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @ApiOperation(value = "Get users count by status")
    @RequestMapping(value = "count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUsersCountByStatus(@RequestParam(value = "removed") boolean removed) {
        Long usersCount = userService.getUsersCountByStatusRemoved(removed);
        return new ResponseEntity<>(usersCount, HttpStatus.OK);
    }

}
