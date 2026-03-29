package ru.tbank.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.practicum.dto.internal.UpdateDeviceSettingsRequest;
import ru.tbank.practicum.dto.internal.UpdateDeviceSettingsResponse;
import ru.tbank.practicum.entity.Device;
import ru.tbank.practicum.entity.DeviceSettings;
import ru.tbank.practicum.mapper.MapperDeviceSettings;
import ru.tbank.practicum.repository.DeviceRepository;
import ru.tbank.practicum.repository.DeviceSettingsRepository;

@Service
@RequiredArgsConstructor
public class SettingsService {
    private final DeviceRepository deviceRepository;

    private final DeviceSettingsRepository deviceSettingsRepository;

    private final MapperDeviceSettings mapperDeviceSettings;

    @Transactional
    public UpdateDeviceSettingsResponse updateSettings(
            Long deviceId, UpdateDeviceSettingsRequest updateDeviceSettingsRequest) {

        DeviceSettings patch = mapperDeviceSettings.toDeviceSettings(updateDeviceSettingsRequest);

        Device device = deviceRepository
                .findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Device not found: " + deviceId));

        DeviceSettings settings = device.getSettings();

        mapperDeviceSettings.updateDeviceSettingsFromRequest(patch, settings);

        DeviceSettings newDeviceSettings = deviceSettingsRepository.save(settings);

        return mapperDeviceSettings.toDeviceSettingsResponse(newDeviceSettings);
    }
}
