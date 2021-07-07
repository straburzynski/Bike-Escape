package com.bikeescape.api.service;

import com.bikeescape.api.model.email.EmailData;

import javax.mail.MessagingException;

public interface EmailService {

    void sendMessage(EmailData email) throws MessagingException;

}
