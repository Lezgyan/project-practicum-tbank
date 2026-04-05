package ru.tbank.practicum.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tbank.practicum.dto.internal.UpdateDeviceSettingsRequest;
import ru.tbank.practicum.dto.internal.UpdateDeviceSettingsResponse;
import ru.tbank.practicum.entity.DeviceSettings;
import ru.tbank.practicum.mapper.MapperDeviceSettings;
import ru.tbank.practicum.repository.DeviceSettingsRepository;

@ExtendWith(MockitoExtension.class)
class SettingsServiceTest {

    @Mock
    private DeviceSettingsRepository deviceSettingsRepository;

    @Mock
    private MapperDeviceSettings mapperDeviceSettings;

    @InjectMocks
    private SettingsService settingsService;

    @Test
    void updateSettings_existingDeviceSettings_appliesPatchAndReturnsSavedResponse() {
        Long deviceId = 10L;

        UpdateDeviceSettingsRequest request = mock(UpdateDeviceSettingsRequest.class);
        UpdateDeviceSettingsResponse response = mock(UpdateDeviceSettingsResponse.class);

        DeviceSettings patch = new DeviceSettings();
        DeviceSettings currentSettings = new DeviceSettings();
        DeviceSettings savedSettings = new DeviceSettings();

        when(mapperDeviceSettings.toDeviceSettings(request)).thenReturn(patch);
        when(deviceSettingsRepository.findByDeviceId(deviceId)).thenReturn(currentSettings);
        when(deviceSettingsRepository.save(currentSettings)).thenReturn(savedSettings);
        when(mapperDeviceSettings.toDeviceSettingsResponse(savedSettings)).thenReturn(response);

        UpdateDeviceSettingsResponse result = settingsService.updateSettings(deviceId, request);

        assertThat(result).isSameAs(response);

        verify(mapperDeviceSettings).toDeviceSettings(request);
        verify(deviceSettingsRepository).findByDeviceId(deviceId);
        verify(mapperDeviceSettings).updateDeviceSettingsFromRequest(patch, currentSettings);
        verify(deviceSettingsRepository).save(currentSettings);
        verify(mapperDeviceSettings).toDeviceSettingsResponse(savedSettings);
    }
}
