package com.user.information.service;

import com.user.information.dto.DeviceRequestTO;
import com.user.information.dto.DeviceResponseTO;
import com.user.information.entity.Device;

import java.util.List;

public interface DeviceService {

    List<DeviceResponseTO> getDevicesForCurrentUser();

    List<DeviceResponseTO> getAllDevices();

    Device addDevice(DeviceRequestTO deviceRequestTO);

    String deleteDeviceByUserId(Long userId);

    String deleteDevice(Long deviceId);
}
