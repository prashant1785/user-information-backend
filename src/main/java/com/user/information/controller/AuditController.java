package com.user.information.controller;

import com.user.information.dto.AuditRequestTO;
import com.user.information.dto.AuditWrapperTO;
import com.user.information.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'DEVELOPER')")
    @PostMapping("/logs")
    public AuditWrapperTO getAuditLogs(@RequestBody AuditRequestTO request) {
        return auditService.getAuditLogs(request);
    }
}
