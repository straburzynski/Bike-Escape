package com.bikeescape.api.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class ApplicationConfiguration {

    @Value("${app.security.issuer}")
    private String securityIssuer;

    @Value("${app.security.secret}")
    private String securitySecret;

    @Value("${app.security.tokenExpirationTime}")
    private int tokenExpirationTime;

    @Value("${app.security.validateTokenIssueDate}")
    private boolean validateTokenIssueDate;

    @Value("${app.security.tokenName}")
    private String tokenName;
}