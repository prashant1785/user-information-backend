package com.user.information.service;

import com.user.information.dto.DeviceRequestTO;
import com.user.information.dto.DeviceResponseTO;
import com.user.information.entity.AuditActionConstant;
import com.user.information.entity.AuditOperation;
import com.user.information.entity.Device;
import com.user.information.entity.User;
import com.user.information.repository.DeviceRepository;
import com.user.information.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditService auditService;

    @Override
    public List<DeviceResponseTO> getDevicesForCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        List<DeviceResponseTO> devices = deviceRepository
                .findByUserId(user.getId())
                .stream()
                .map(this::mapToDto)
                .toList();

        auditService.save("Devices fetched for user: " + email + ", count: " + devices.size(), "Device",
                AuditActionConstant.RECORD_FETCHED,
                AuditOperation.FETCH.name()
        );

        return devices;
    }

    @Override
    public List<DeviceResponseTO> getAllDevices() {
        List<DeviceResponseTO> devices = deviceRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();

        auditService.save("All devices fetched, count: " + devices.size(), "Device",
                AuditActionConstant.RECORD_FETCHED,
                AuditOperation.FETCH.name()
        );

        return devices;
    }

    @Override
    public Device addDevice(DeviceRequestTO request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Device device = Device.builder()
                .deviceId(request.getDeviceId())
                .eventType(request.getEventType())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .details(request.getDetails())
                .user(user)
                .build();

        Device saved = deviceRepository.save(device);

        auditService.save("New device added: deviceId='" + saved.getDeviceId() + "' for userId=" + user.getId(), "Device",
                AuditActionConstant.NEW_RECORD_INSERTED,
                AuditOperation.INSERT.name()
        );

        return saved;
    }

    @Transactional
    @Override
    public String deleteDeviceByUserId(Long userId) {
        deviceRepository.deleteByUserId(userId);
        auditService.save("All devices deleted for userId=" + userId, "Device",
                AuditActionConstant.RECORD_DELETED,
                AuditOperation.DELETE.name()
        );
        return "Device delete successfully";
    }

    @Override
    public String deleteDevice(Long deviceId) {
        deviceRepository.deleteById(deviceId);
        auditService.save("Device deleted: deviceId=" + deviceId, "Device",
                AuditActionConstant.RECORD_DELETED,
                AuditOperation.DELETE.name()
        );

        return "Device delete successfully";
    }

    private DeviceResponseTO mapToDto(Device device) {

        DeviceResponseTO dto = new DeviceResponseTO();

        dto.setId(device.getId());
        dto.setDeviceId(device.getDeviceId());
        dto.setEventType(device.getEventType());
        dto.setStartTime(device.getStartTime());
        dto.setEndTime(device.getEndTime());
        dto.setDetails(device.getDetails());

        dto.setUserId(device.getUser().getId());
        dto.setUserName(device.getUser().getFullName());
        dto.setUserEmail(device.getUser().getEmail());

        return dto;
    }
}
