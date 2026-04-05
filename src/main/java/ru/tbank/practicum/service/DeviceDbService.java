package ru.tbank.practicum.service;

import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.dto.internal.DtoCreateDevice;
import ru.tbank.practicum.dto.internal.DtoCreateDeviceResponse;
import ru.tbank.practicum.entity.Device;
import ru.tbank.practicum.entity.DeviceSettings;
import ru.tbank.practicum.entity.DeviceState;
import ru.tbank.practicum.entity.statePayload.BlindsStatePayload;
import ru.tbank.practicum.entity.statePayload.DeviceStatePayload;
import ru.tbank.practicum.entity.statePayload.RadiatorStatePayload;
import ru.tbank.practicum.enums.DeviceType;
import ru.tbank.practicum.mapper.MapperDevice;
import ru.tbank.practicum.repository.DeviceRepository;

@Service
@RequiredArgsConstructor
public class DeviceDbService {
    private final DeviceRepository deviceRepository;

    private final MapperDevice mapperDevice;

    @Transactional
    public DtoCreateDeviceResponse createDevice(DtoCreateDevice dtoCreateDevice) {
        Device device = mapperDevice.mapToDeviceEntity(dtoCreateDevice);
        device.setSettings(new DeviceSettings());
        device.setExternalId(UUID.randomUUID());

        DeviceState deviceState = new DeviceState();
        deviceState.setDeviceStatePayload(getStatePayload(device));

        device.setDeviceState(deviceState);

        Device newDevice = deviceRepository.save(device);

        return mapperDevice.mapToCreateDeviceResponse(newDevice);
    }

    private DeviceStatePayload getStatePayload(Device device) {
        DeviceType deviceType = device.getType();

        return switch (deviceType) {
            case DeviceType.RADIATOR -> new RadiatorStatePayload();
            case DeviceType.BLINDS -> new BlindsStatePayload();
        };
    }
}
