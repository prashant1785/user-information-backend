package com.user.information.controller;

import com.user.information.dto.DeviceRequestTO;
import com.user.information.dto.DeviceResponseTO;
import com.user.information.entity.Device;
import com.user.information.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/my")
    public List<DeviceResponseTO> myDevices() {
        return deviceService.getDevicesForCurrentUser();
    }

    @GetMapping("/all")
    public List<DeviceResponseTO> allDevices() {
        return deviceService.getAllDevices();
    }

    @PostMapping("/add")
    public Device addDevice(@Valid @RequestBody DeviceRequestTO request) {

        return deviceService.addDevice(request);
    }

    @DeleteMapping("/delete-by-user-id/{userID}")
    public String deleteDeviceByUserId(@PathVariable Long userID) {

        return deviceService.deleteDeviceByUserId(userID);
    }

    @DeleteMapping("/delete/{deviceId}")
    public String deleteDevice(@PathVariable Long deviceId) {

        return deviceService.deleteDevice(deviceId);
    }
}