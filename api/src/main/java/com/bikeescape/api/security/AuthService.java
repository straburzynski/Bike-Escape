package com.bikeescape.api.security;

import com.bikeescape.api.exceptions.AuthenticationException;
import com.bikeescape.api.exceptions.UserNotFoundException;
import com.bikeescape.api.model.user.UserLoginTO;
import com.bikeescape.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {

    private final UserService userService;
    private TokenAuthenticationService tokenAuthenticationService;
    private BCryptPasswordEncoder passwordEncoder2;

    @Autowired
    AuthService(UserService userService,
                @Lazy TokenAuthenticationService tokenAuthenticationService,
                @Qualifier("passwordEncoder2") BCryptPasswordEncoder bCryptPasswordEncoder2) {
        this.userService = userService;
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.passwordEncoder2 = bCryptPasswordEncoder2;
    }

    @Override
    public final User loadUserByUsername(String email) throws UsernameNotFoundException {
        final com.bikeescape.api.model.user.User user = getUserByEmail(email);
        return new User(getUserLoginIdentity(user), UUID.randomUUID().toString(), user.getAuthorities());
    }

    public void login(UserLoginTO userLoginTo, HttpServletResponse response) {
        com.bikeescape.api.model.user.User dbUser = getUserByEmail(userLoginTo.getEmail());
        if (!passwordEncoder2.matches(userLoginTo.getPassword(), dbUser.getPassword())) {
            throw new AuthenticationException("Incorrect password");
        }
        User user = loadUserByUsername(userLoginTo.getEmail());
        UserAuthentication authentication = new UserAuthentication(user);
        tokenAuthenticationService.addAuthentication(response, authentication);
    }

     public void login(com.bikeescape.api.model.user.User dbUser, HttpServletResponse response) {
        User user = loadUserByUsername(dbUser.getEmail());
        UserAuthentication authentication = new UserAuthentication(user);
        tokenAuthenticationService.addAuthentication(response, authentication);
    }

    private String getUserLoginIdentity(com.bikeescape.api.model.user.User user) {
        return user.getEmail();
    }

    private com.bikeescape.api.model.user.User getUserByEmail(String email) {
        com.bikeescape.api.model.user.User user = userService.findByUsernameOrEmail(email.toLowerCase());
        if (user == null || user.getId() == null) {
            throw new UserNotFoundException("Incorrect login or password");
        }
        return user;
    }

    public com.bikeescape.api.model.user.User getLoggedUser(HttpServletRequest request) {
        String email = tokenAuthenticationService.getLoggedUsername(request);
        return getUserByEmail(email);
    }
}