package ru.tbank.practicum.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tbank.practicum.dto.internal.DtoCreateDevice;
import ru.tbank.practicum.dto.internal.DtoCreateDeviceResponse;
import ru.tbank.practicum.dto.internal.NewDeviceStateResponse;
import ru.tbank.practicum.dto.internal.UpdateDeviceSettingsRequest;
import ru.tbank.practicum.dto.internal.UpdateDeviceSettingsResponse;
import ru.tbank.practicum.service.DeviceDbService;
import ru.tbank.practicum.service.DeviceStateQueryService;
import ru.tbank.practicum.service.SettingsService;

@RestController
@Slf4j
@RequestMapping("/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final SettingsService settingsService;

    private final DeviceDbService deviceDbService;

    private final DeviceStateQueryService deviceStateQueryService;

    @PutMapping("/{deviceId}/settings")
    public ResponseEntity<UpdateDeviceSettingsResponse> updateSettings(
            @PathVariable Long deviceId, @RequestBody @Valid UpdateDeviceSettingsRequest request) {

        UpdateDeviceSettingsResponse saved = settingsService.updateSettings(deviceId, request);

        return ResponseEntity.ok(saved);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createDevice(@RequestBody @Valid DtoCreateDevice dtoCreateDevice) {
        DtoCreateDeviceResponse dtoCreateDeviceResponse = deviceDbService.createDevice(dtoCreateDevice);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoCreateDeviceResponse);
    }

    @GetMapping("/states")
    public ResponseEntity<List<NewDeviceStateResponse>> getDeviceStates() {
        return ResponseEntity.ok(deviceStateQueryService.getDeviceStates());
    }
}
