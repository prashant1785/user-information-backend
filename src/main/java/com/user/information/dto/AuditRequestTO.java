package com.user.information.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditRequestTO {

    private String searchKey;           // search across description, entity, username
    private String sortColumn;          // "timestamp", "username", "entityName", "operation"
    private String sortWay;             // "asc" or "desc"
    private int pageSize;
    private int pageNo;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime toDate;

    private String operationFilter;     // INSERT, UPDATE, DELETE, FETCH
    private String usernameFilter;
    private String entityFilter;
}
