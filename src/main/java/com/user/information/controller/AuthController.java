package com.user.information.controller;

import com.user.information.dto.*;
import com.user.information.entity.User;
import com.user.information.repository.UserRepository;
import com.user.information.service.JwtServiceImpl;
import com.user.information.service.OtpService;
import com.user.information.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtServiceImpl jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpService otpService;

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequestTO request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public AuthResponseTO login(@Valid @RequestBody LoginRequestTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new AuthResponseTO(accessToken, refreshToken);
    }
    @PostMapping("/login-otp")
    public MessageResponseTO loginWithOtp(@Valid @RequestBody LoginRequestTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // password is valid, now send OTP
        otpService.generateAndSendOtp(request.getEmail());

        return new MessageResponseTO("OTP sent to your registered email");
    }

    // ── OTP Login Step 2 — verify OTP and return tokens ──
    @PostMapping("/verify-otp")
    public AuthResponseTO verifyOtp(@RequestBody OtpVerifyRequestTO request) {

        boolean valid = otpService.verifyOtp(
                request.getEmail(),
                request.getOtpCode()
        );

        if (!valid) {
            throw new RuntimeException("Invalid or expired OTP");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String accessToken  = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new AuthResponseTO(accessToken, refreshToken);
    }

    // ── Resend OTP ──
    @PostMapping("/resend-otp")
    public MessageResponseTO resendOtp(@RequestBody OtpRequestTO request) {

        otpService.generateAndSendOtp(request.getEmail());

        return new MessageResponseTO("OTP resent to your registered email");
    }

    @PostMapping("/refresh")
    public AuthResponseTO refreshToken(@RequestBody RefreshTokenRequestTO request) {

        String email = jwtService.extractEmail(request.getRefreshToken());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String accessToken = jwtService.generateAccessToken(user);

        return new AuthResponseTO(
                accessToken,
                request.getRefreshToken()
        );
    }
}