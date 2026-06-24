package com.user.information.controller;

import com.user.information.dto.RegisterRequestTO;
import com.user.information.dto.RoleUpdateRequestTO;
import com.user.information.entity.User;
import com.user.information.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/super-admin")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'DEVELOPER')")
    @PutMapping("/users/{id}/role")
    public User updateUserRole(@PathVariable Long id, @RequestBody RoleUpdateRequestTO request) {

        return userService.updateRole(id, request.getRole());
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'DEVELOPER')")
    @DeleteMapping("/users/delete/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'DEVELOPER')")
    @PostMapping("/users/create")
    public User createUser(@RequestBody RegisterRequestTO request) {
        return userService.registerByAdmin(request);
    }
}
