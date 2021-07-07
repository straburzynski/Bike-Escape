package com.bikeescape.api.config;

import com.bikeescape.api.ApiApplication;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@ConfigurationProperties(prefix = "app")
@Component
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public MailSender mailSenderImplementation() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        InputStream stream = ApiApplication.class.getClassLoader().getResourceAsStream("mail.yml");
        Properties properties = new Properties();
        try {
            properties.load(stream);
            mailSender.setJavaMailProperties(properties);
        } catch (IOException exception) {
            return null;
        }

        javax.mail.Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(properties.getProperty("username"), properties.getProperty("password"));
                    }
                });

        mailSender.setSession(session);

        return mailSender;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder2() {
        return new BCryptPasswordEncoder();
    }
}