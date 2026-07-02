package com.user.information.repository;

import com.user.information.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AuditRepository extends JpaRepository<AuditLog, Long> {

    @Query(value = """
            SELECT * FROM audit_logs a
            WHERE
                (CAST(:searchKey AS text) IS NULL OR
                    LOWER(a.description::text) LIKE LOWER(CONCAT('%', CAST(:searchKey AS text), '%')) OR
                    LOWER(a.entity_name::text) LIKE LOWER(CONCAT('%', CAST(:searchKey AS text), '%')) OR
                    LOWER(a.username::text)    LIKE LOWER(CONCAT('%', CAST(:searchKey AS text), '%'))
                )
                AND (CAST(:operationFilter AS text) IS NULL OR a.operation  = :operationFilter)
                AND (CAST(:usernameFilter  AS text) IS NULL OR a.username   = :usernameFilter)
                AND (CAST(:entityFilter    AS text) IS NULL OR a.entity_name = :entityFilter)
                AND (CAST(:fromDate AS timestamp) IS NULL OR a.timestamp >= :fromDate)
                AND (CAST(:toDate   AS timestamp) IS NULL OR a.timestamp <= :toDate)
            """,
            countQuery = """
                    SELECT COUNT(*) FROM audit_logs a
                    WHERE
                        (CAST(:searchKey AS text) IS NULL OR
                            LOWER(a.description::text) LIKE LOWER(CONCAT('%', CAST(:searchKey AS text), '%')) OR
                            LOWER(a.entity_name::text) LIKE LOWER(CONCAT('%', CAST(:searchKey AS text), '%')) OR
                            LOWER(a.username::text)    LIKE LOWER(CONCAT('%', CAST(:searchKey AS text), '%'))
                        )
                        AND (CAST(:operationFilter AS text) IS NULL OR a.operation   = :operationFilter)
                        AND (CAST(:usernameFilter  AS text) IS NULL OR a.username    = :usernameFilter)
                        AND (CAST(:entityFilter    AS text) IS NULL OR a.entity_name = :entityFilter)
                        AND (CAST(:fromDate AS timestamp) IS NULL OR a.timestamp >= :fromDate)
                        AND (CAST(:toDate   AS timestamp) IS NULL OR a.timestamp <= :toDate)
                    """,
            nativeQuery = true)
    Page<AuditLog> findAllWithFilters(
            @Param("searchKey") String searchKey,
            @Param("operationFilter") String operationFilter,
            @Param("usernameFilter") String usernameFilter,
            @Param("entityFilter") String entityFilter,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable
    );
}
