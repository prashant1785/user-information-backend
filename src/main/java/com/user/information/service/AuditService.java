package com.user.information.service;

import com.user.information.dto.AuditRequestTO;
import com.user.information.dto.AuditResponseTO;
import com.user.information.dto.AuditTO;
import com.user.information.dto.AuditWrapperTO;
import com.user.information.entity.AuditLog;
import com.user.information.repository.AuditRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class AuditService {

    @Autowired
    private AuditRepository auditRepository;

    public void save(AuditTO auditTO) {
        try {
            String currentUser = resolveCurrentUser();

            AuditLog log = AuditLog.builder()
                    .username(currentUser)
                    .entityName(auditTO.getEntityName())
                    .operation(auditTO.getOperation())
                    .action(auditTO.getAction())
                    .description(auditTO.getDescription())
                    .ipAddress(resolveIpAddress())
                    .timestamp(LocalDateTime.now())
                    .build();

            auditRepository.save(log);

        } catch (Exception ex) {
            log.error("Audit logging failed: {}", ex.getMessage(), ex);
        }
    }

    public void save(String description,
                     String entityName,
                     String action,
                     String operation) {
        save(AuditTO.builder()
                .description(description)
                .entityName(entityName)
                .action(action)
                .operation(operation)
                .build());
    }

    private String resolveCurrentUser() {
        try {
            return SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getName();
        } catch (Exception e) {
            return "SYSTEM";
        }
    }

    private String resolveIpAddress() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes()).getRequest();

            String ip = request.getHeader("X-Forwarded-For");

            if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Real-IP");
            }

            if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }

            if (ip != null && ip.contains(",")) {
                ip = ip.split(",")[0].trim();
            }

            return ip;

        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    public AuditWrapperTO getAuditLogs(AuditRequestTO request) {

        int pageNo = request.getPageNo() > 0 ? request.getPageNo() - 1 : 0;
        int pageSize = request.getPageSize() > 0 ? request.getPageSize() : 10;

        String sortColumn = (request.getSortColumn() != null
                && !request.getSortColumn().isBlank())
                ? request.getSortColumn() : "timestamp";

        Sort sort = "asc".equalsIgnoreCase(request.getSortWay())
                ? Sort.by(sortColumn).ascending()
                : Sort.by(sortColumn).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        String searchKey = blankToNull(request.getSearchKey());
        String operationFilter = blankToNull(request.getOperationFilter());
        String usernameFilter = blankToNull(request.getUsernameFilter());
        String entityFilter = blankToNull(request.getEntityFilter());

        Page<AuditLog> page = auditRepository.findAllWithFilters(
                searchKey, operationFilter, usernameFilter, entityFilter,
                request.getFromDate(), request.getToDate(),
                pageable
        );

        List<AuditResponseTO> logs = page.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return AuditWrapperTO.builder()
                .auditLogs(logs)
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .pageSize(pageSize)
                .pageNo(pageNo + 1)
                .build();
    }

    private AuditResponseTO mapToResponse(AuditLog log) {
        return AuditResponseTO.builder()
                .id(log.getId())
                .username(log.getUsername())
                .entityName(log.getEntityName())
                .operation(log.getOperation())
                .action(log.getAction())
                .description(log.getDescription())
                .ipAddress(log.getIpAddress())
                .timestamp(log.getTimestamp())
                .build();
    }

    private String blankToNull(String value) {
        return (value != null && !value.isBlank()) ? value : null;
    }
}
