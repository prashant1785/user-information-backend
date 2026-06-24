package com.user.information.service;

import com.user.information.dto.RegisterRequestTO;
import com.user.information.entity.*;
import com.user.information.repository.DeviceRepository;
import com.user.information.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private AuditService auditService;

    @Override
    public User register(RegisterRequestTO request) {

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .password(encoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        User saved = repository.save(user);
        auditService.save("New user registered: " + saved.getEmail(), "User", AuditActionConstant.NEW_RECORD_INSERTED, AuditOperation.INSERT.name());

        return saved;
    }

    @Override
    public User updateRole(Long id, Role role) {

        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String oldRole = user.getRole().name();
        user.setRole(role);
        User saved = repository.save(user);

        auditService.save("Role changed for user '" + user.getEmail() + "' from " + oldRole + " to " + role.name(), "User",
                AuditActionConstant.RECORD_UPDATED,
                AuditOperation.UPDATE.name()
        );

        return saved;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> ls = repository.findAll();

        auditService.save("All users fetched, count: " + ls.size(), "User",
                AuditActionConstant.RECORD_FETCHED,
                AuditOperation.FETCH.name()
        );

        return ls;
    }

    @Transactional
    @Override
    public String deleteUser(Long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        deviceService.deleteDeviceByUserId(userId);
        repository.deleteById(userId);

        auditService.save("User deleted: " + user.getEmail() + " (id=" + userId + ")", "User",
                AuditActionConstant.RECORD_DELETED,
                AuditOperation.DELETE.name()
        );
        return "User delete successfully";
    }

    @Override
    public User registerByAdmin(RegisterRequestTO request) {
        if (request.getRole() == null) {
            throw new RuntimeException("Role is required");
        }
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .password(encoder.encode(request.getPassword()))
                .role(request.getRole())          // ← uses provided role
                .build();

        User saved = repository.save(user);

        auditService.save("New user created by admin: " + saved.getEmail() + " with role: " + saved.getRole().name(), "User",
                AuditActionConstant.NEW_RECORD_INSERTED,
                AuditOperation.INSERT.name()
        );

        return saved;
    }
}

