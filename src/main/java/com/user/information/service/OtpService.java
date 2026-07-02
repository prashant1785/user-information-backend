package com.user.information.service;

public interface OtpService {

    void generateAndSendOtp(String email);

    boolean verifyOtp(String email, String otpCode);
}
