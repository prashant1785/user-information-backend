package com.user.information.dto;

import lombok.Data;

@Data
public class OtpVerifyRequestTO {
    private String email;
    private String otpCode;
}
