package com.bikeescape.api.security;

import com.bikeescape.api.config.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class TokenAuthenticationService {

    private final ApplicationConfiguration applicationConfiguration;
    private final TokenHandler tokenHandler;

    @Autowired
    TokenAuthenticationService(TokenHandler tokenHandler, ApplicationConfiguration applicationConfiguration) {
        this.tokenHandler = tokenHandler;
        this.applicationConfiguration = applicationConfiguration;
    }

    public String addAuthentication(HttpServletResponse response, UserAuthentication authentication) {
        final User user = authentication.getDetails();
        String token = tokenHandler.createTokenForUser(user);
        response.addHeader(applicationConfiguration.getTokenName(), token);
        return token;
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(applicationConfiguration.getTokenName());
        if (token != null) {
            final User user = tokenHandler.parseUserFromToken(token);
            return new UserAuthentication(user);
        }
        return null;
    }

    public String getLoggedUsername(HttpServletRequest request) {
        Authentication auth = getAuthentication(request);
        if (auth != null) {
            return auth.getName();
        }
        return null;
    }
}