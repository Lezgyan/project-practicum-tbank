package ru.tbank.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tbank.practicum.dto.internal.UpdateRadiatorStateRequest;
import ru.tbank.practicum.enums.AutoDeviceCommand;
import ru.tbank.practicum.service.DeviceCommandService;

@RestController
@RequiredArgsConstructor
@RequestMapping("radiator")
public class RadiatorController {

    private final DeviceCommandService deviceCommandService;

    @PostMapping("/{deviceId}/state")
    public ResponseEntity<String> setRadiatorState(
            @PathVariable Long deviceId, @RequestBody UpdateRadiatorStateRequest request) {

        deviceCommandService.setRadiatorTemperature(
                deviceId, request.targetTemperature(), AutoDeviceCommand.HANDLE_CHANGE);
        return ResponseEntity.ok("Radiator state command sent");
    }
}
