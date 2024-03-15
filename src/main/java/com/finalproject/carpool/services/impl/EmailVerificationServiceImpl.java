package com.finalproject.carpool.services.impl;

import com.finalproject.carpool.models.User;
import com.finalproject.carpool.repositories.UserRepository;
import com.finalproject.carpool.services.EmailVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    @Autowired
    public EmailVerificationServiceImpl(JavaMailSender mailSender, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
    }

    @Override
    public void sendVerificationEmail(User user, String siteUrl) {

        String recipientAddress = user.getEmail();
        String subject = "Email Verification";
        String confirmationUrl = "http://localhost:8080" + "/verify-email?token=" + user.getVerificationCode();
        String message = "Click the link below to verify your email:\n" + confirmationUrl;

        if (recipientAddress != null && !recipientAddress.isEmpty()) {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(recipientAddress);
            email.setSubject(subject);
            email.setText(message);

            mailSender.send(email);
        } else {
            throw new IllegalArgumentException("Recipient address is null or empty");
        }
    }
    @Override
    public void verifyEmail(String code) {
        User user = userRepository.findByVerificationCode(code);
        user.setVerificationCode(code);
        user.setVerified(true);
//        userRepository.create(user);
    }

}
