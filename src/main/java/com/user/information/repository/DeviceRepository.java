package com.user.information.repository;

import com.user.information.entity.Device;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM devices d WHERE d.user.id = :userId")
    Long deleteByUserId(@Param("userId") Long userId);
}
