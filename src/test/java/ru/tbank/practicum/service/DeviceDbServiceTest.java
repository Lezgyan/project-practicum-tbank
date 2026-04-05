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
import ru.tbank.practicum.dto.internal.DtoCreateDevice;
import ru.tbank.practicum.dto.internal.DtoCreateDeviceResponse;
import ru.tbank.practicum.entity.Device;
import ru.tbank.practicum.entity.DeviceSettings;
import ru.tbank.practicum.entity.DeviceState;
import ru.tbank.practicum.entity.statePayload.BlindsStatePayload;
import ru.tbank.practicum.entity.statePayload.RadiatorStatePayload;
import ru.tbank.practicum.enums.DeviceType;
import ru.tbank.practicum.mapper.MapperDevice;
import ru.tbank.practicum.repository.DeviceRepository;

@ExtendWith(MockitoExtension.class)
class DeviceDbServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private MapperDevice mapperDevice;

    @InjectMocks
    private DeviceDbService deviceDbService;

    @Test
    void createDevice_radiatorType_initializesDefaultSettingsStateAndExternalId() {
        DtoCreateDevice request = mock(DtoCreateDevice.class);

        Device device = new Device();
        device.setType(DeviceType.RADIATOR);

        Device savedDevice = new Device();
        DtoCreateDeviceResponse response = mock(DtoCreateDeviceResponse.class);

        when(mapperDevice.mapToDeviceEntity(request)).thenReturn(device);
        when(deviceRepository.save(device)).thenReturn(savedDevice);
        when(mapperDevice.mapToCreateDeviceResponse(savedDevice)).thenReturn(response);

        DtoCreateDeviceResponse result = deviceDbService.createDevice(request);

        assertThat(result).isSameAs(response);
        assertThat(device.getSettings()).isNotNull().isInstanceOf(DeviceSettings.class);
        assertThat(device.getExternalId()).isNotNull();
        assertThat(device.getDeviceState()).isNotNull().isInstanceOf(DeviceState.class);
        assertThat(device.getDeviceState().getDeviceStatePayload()).isInstanceOf(RadiatorStatePayload.class);

        verify(deviceRepository).save(device);
    }

    @Test
    void createDevice_blindsType_initializesDefaultSettingsStateAndExternalId() {
        DtoCreateDevice request = mock(DtoCreateDevice.class);

        Device device = new Device();
        device.setType(DeviceType.BLINDS);

        Device savedDevice = new Device();
        DtoCreateDeviceResponse response = mock(DtoCreateDeviceResponse.class);

        when(mapperDevice.mapToDeviceEntity(request)).thenReturn(device);
        when(deviceRepository.save(device)).thenReturn(savedDevice);
        when(mapperDevice.mapToCreateDeviceResponse(savedDevice)).thenReturn(response);

        deviceDbService.createDevice(request);

        assertThat(device.getSettings()).isNotNull();
        assertThat(device.getExternalId()).isNotNull();
        assertThat(device.getDeviceState()).isNotNull();
        assertThat(device.getDeviceState().getDeviceStatePayload()).isInstanceOf(BlindsStatePayload.class);
    }
}
