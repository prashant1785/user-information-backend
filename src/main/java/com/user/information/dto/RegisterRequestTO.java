package com.user.information.dto;

import com.user.information.entity.Role;
import lombok.Data;

@Data
public class RegisterRequestTO {

    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String password;
    private Role role;
}
