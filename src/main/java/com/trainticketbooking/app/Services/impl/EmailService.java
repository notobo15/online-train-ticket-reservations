package com.trainticketbooking.app.Services.impl;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendResetPasswordEmail(String toEmail, String token) throws MessagingException, UnsupportedEncodingException {
        String subject = "Reset Password";
        String resetPasswordLink = "http://localhost:8080/reset-password?token=" + token;
        String body = "<p>Dear user,</p>" +
                "<p>You have requested to reset your password. Please click the link below to reset it:</p>" +
                "<p><a href=\"" + resetPasswordLink + "\">Reset my password</a></p>" +
                "<p>If you did not request this, please ignore this email.</p>" +
                "<p>Best regards,<br>Your Company</p>";

        sendEmail(toEmail, subject, body, true);
    }

    @Async
    public void sendEmail(String to, String subject, String body, boolean isHtml) throws MessagingException, UnsupportedEncodingException, IllegalArgumentException {
        if (fromEmail == null || fromEmail.isEmpty()) {
            throw new IllegalArgumentException("From address must not be null or empty");
        }
        if (to == null || to.isEmpty()) {
            throw new IllegalArgumentException("Recipient address must not be null or empty");
        }
        if (subject == null || subject.isEmpty()) {
            throw new IllegalArgumentException("Subject must not be null or empty");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setPriority(1);
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, isHtml);
        mailSender.send(message);
    }
}