package com.bikeescape.api.security;

import com.bikeescape.api.config.ApplicationConfiguration;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public final class TokenHandler {

    private ApplicationConfiguration applicationConfiguration;
    private AuthService authService;

    @Autowired
    public TokenHandler(ApplicationConfiguration applicationConfiguration, AuthService authService) {
        this.applicationConfiguration = applicationConfiguration;
        this.authService = authService;
    }

    public User parseUserFromToken(String token) {
        String username = Jwts.parser()
                .setSigningKey(getSecret())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return authService.loadUserByUsername(username);
    }

    public String createTokenForUser(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + (applicationConfiguration.getTokenExpirationTime() * 1000L));
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setIssuer(applicationConfiguration.getSecurityIssuer())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, getSecret())
                .compact();
    }

    private String getSecret() {
        return Base64.getEncoder().encodeToString(applicationConfiguration.getSecuritySecret().getBytes());
    }
}