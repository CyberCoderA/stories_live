package com.heydrian.stories_live.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.security.SecureRandom;

@Service 
public class EmailService {
    private final SecureRandom random = new SecureRandom();
    
    @Autowired 
    private JavaMailSender mailSender;

    @Value("${MAIL_USERNAME}")
    private String fromUsername;

    public String generateOtp() {
        return String.format("%06d", random.nextInt(1_000_000));
    }

    public void sendVerificationEmail(String to, String verificationCode) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromUsername);
            helper.setTo(to);
            helper.setSubject("Verify your Stories Live account");
            helper.setText("Your Stories Live verification code is: " + verificationCode);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
