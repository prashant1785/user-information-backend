package com.user.information.service;

import com.user.information.dto.RegisterRequestTO;
import com.user.information.entity.Role;
import com.user.information.entity.User;

import java.util.List;

public interface UserService {
    User register(RegisterRequestTO request);

    User updateRole(Long id, Role role);

    List<User> getAllUsers();

    String deleteUser(Long userId);

    User registerByAdmin(RegisterRequestTO request);
}
