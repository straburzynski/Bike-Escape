package com.bikeescape.api.converter;

import com.bikeescape.api.model.user.User;
import com.bikeescape.api.model.user.UserLoggedInTO;
import com.bikeescape.api.model.user.UserRegistrationTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserConverter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserConverter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserLoggedInTO convertUserToLoggedInUser(User user) {
        return UserLoggedInTO.builder()
                .id(user.getId())
                .authorities(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .build();
    }

    public User convertUserRegistrationTOToUser(UserRegistrationTO userRegistrationTO) {
        User user = new User();
        user.setFirstName(userRegistrationTO.getFirstName());
        user.setEmail(userRegistrationTO.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(userRegistrationTO.getPassword()));
        return user;
    }

}
