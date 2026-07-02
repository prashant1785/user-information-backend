package com.user.information.dto;

import com.user.information.entity.EventType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeviceResponseTO {
    private Long id;
    private String deviceId;
    private EventType eventType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String details;

    private Long userId;
    private String userName;
    private String userEmail;
}
