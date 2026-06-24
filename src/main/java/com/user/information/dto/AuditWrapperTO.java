package com.user.information.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditWrapperTO {
    private List<AuditResponseTO> auditLogs;
    private long totalItems;
    private long totalPages;
    private int pageSize;
    private int pageNo;
}
