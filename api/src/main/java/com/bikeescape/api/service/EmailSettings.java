package com.bikeescape.api.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;

@Data
@Configuration
public class EmailSettings {

    @Value("${mail.from}")
    private String from;

    private final MailSender mailSender;

    @Autowired
    public EmailSettings(MailSender mailSender) {
        this.mailSender = mailSender;
    }
}
