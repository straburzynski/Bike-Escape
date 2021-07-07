package com.bikeescape.api.service;

import com.bikeescape.api.model.email.EmailAttachmentData;
import com.bikeescape.api.model.email.EmailData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    private EmailSettings emailSettings;

    @Autowired
    public EmailServiceImpl(EmailSettings emailSettings) {
        this.emailSettings = emailSettings;
    }

    public void sendMessage(EmailData data) throws MessagingException {
        MailSender mailSender = emailSettings.getMailSender();
        MimeMessage message = ((JavaMailSenderImpl) mailSender).createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setTo(data.getRecipients());
        messageHelper.setFrom(emailSettings.getFrom());
        messageHelper.setText(data.getMessage());
        messageHelper.setSubject(data.getSubject());

        EmailAttachmentData[] attachments = data.getAttachments();
        if (attachments != null && attachments.length > 0) {
            for (EmailAttachmentData attachment : data.getAttachments()) {
                byte[] body = attachment.getAttachment();
                messageHelper.addAttachment(attachment.getAttachmentName(), new ByteArrayResource(body));
            }
        }
        ((JavaMailSenderImpl) mailSender).send(message);
    }
}
