package com.heydrian.stories_live.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
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
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromUsername);
        message.setTo(to);
        message.setSubject("Verify your Stories Live account");
        message.setText("Your Stories Live verification code is: " + verificationCode);

        mailSender.send(message);
    }
}
