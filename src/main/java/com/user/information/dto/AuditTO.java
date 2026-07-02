package com.user.information.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditTO {
    private String username;
    private String entityName;
    private String operation;
    private String action;
    private String description;
    private String ipAddress;
    private LocalDateTime timestamp;
}
