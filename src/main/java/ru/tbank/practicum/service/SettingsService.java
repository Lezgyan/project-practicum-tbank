package ru.tbank.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.practicum.dto.internal.UpdateDeviceSettingsRequest;
import ru.tbank.practicum.dto.internal.UpdateDeviceSettingsResponse;
import ru.tbank.practicum.entity.DeviceSettings;
import ru.tbank.practicum.mapper.MapperDeviceSettings;
import ru.tbank.practicum.repository.DeviceSettingsRepository;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final DeviceSettingsRepository deviceSettingsRepository;

    private final MapperDeviceSettings mapperDeviceSettings;

    @Transactional
    public UpdateDeviceSettingsResponse updateSettings(
            Long deviceId, UpdateDeviceSettingsRequest updateDeviceSettingsRequest) {

        DeviceSettings patch = mapperDeviceSettings.toDeviceSettings(updateDeviceSettingsRequest);

        DeviceSettings deviceSettings = deviceSettingsRepository.findByDeviceId(deviceId);

        mapperDeviceSettings.updateDeviceSettingsFromRequest(patch, deviceSettings);

        DeviceSettings newDeviceSettings = deviceSettingsRepository.save(deviceSettings);

        return mapperDeviceSettings.toDeviceSettingsResponse(newDeviceSettings);
    }
}
