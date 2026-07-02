package com.user.information.repository;

import com.user.information.entity.OtpEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpEntry, Long> {

    Optional<OtpEntry> findTopByEmailOrderByCreatedAtDesc(String email);

    void deleteByEmail(String email);
}
