package com.user.information.service;

import com.user.information.entity.OtpEntry;
import com.user.information.repository.OtpRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Slf4j
@Service
public class OtpServiceImpl implements OtpService {

    private final OtpRepository otpRepository;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${otp.expiration-minutes:5}")
    private int expirationMinutes;

    private final SecureRandom random = new SecureRandom();

    public OtpServiceImpl(OtpRepository otpRepository,
                          JavaMailSender mailSender) {
        this.otpRepository = otpRepository;
        this.mailSender = mailSender;
    }

    @Transactional
    @Override
    public void generateAndSendOtp(String email) {

        otpRepository.deleteByEmail(email);

        String otpCode = String.format("%06d", random.nextInt(999999));

        OtpEntry entry = OtpEntry.builder()
                .email(email)
                .otpCode(otpCode)
                .expiresAt(LocalDateTime.now().plusMinutes(expirationMinutes))
                .verified(false)
                .build();

        otpRepository.save(entry);

        log.info("OTP generated for email: {}", email);

        sendOtpEmail(email, otpCode);
    }

    @Override
    public boolean verifyOtp(String email, String otpCode) {

        OtpEntry entry = otpRepository
                .findTopByEmailOrderByCreatedAtDesc(email)
                .orElse(null);

        if (entry == null) return false;
        if (entry.isVerified()) return false;
        if (entry.getExpiresAt().isBefore(LocalDateTime.now())) return false;
        if (!entry.getOtpCode().equals(otpCode)) return false;

        entry.setVerified(true);
        otpRepository.save(entry);

        return true;
    }

    @Async
    public void sendOtpEmail(String toEmail, String otpCode) {
        try {
            log.info("Sending OTP email to: {}", toEmail);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("domkawaleprashant1785@gmail.com");    // ← full email with @
            message.setTo(toEmail);
            message.setSubject("Your Login OTP Code");
            message.setText(
                    "Your OTP code is: " + otpCode +
                            "\n\nThis code expires in " + expirationMinutes + " minutes." +
                            "\n\nIf you did not request this, ignore this email."
            );
            mailSender.send(message);

            log.info("OTP email sent successfully to: {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send OTP email to {}: {}", toEmail, e.getMessage(), e);
        }
    }
}