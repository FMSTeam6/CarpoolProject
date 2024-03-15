package com.finalproject.carpool.services;

import com.finalproject.carpool.models.User;

public interface EmailVerificationService {
    void sendVerificationEmail(User user, String siteUrl);

    void verifyEmail(String code);
}
