package ru.tbank.practicum.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.dto.internal.NewDeviceStateResponse;
import ru.tbank.practicum.entity.Device;
import ru.tbank.practicum.entity.DeviceState;
import ru.tbank.practicum.repository.DeviceStateRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceStateQueryService {
    private final DeviceStateRepository deviceStateRepository;

    public List<NewDeviceStateResponse> getDeviceStates() {
        return deviceStateRepository.findAllForApi().stream().map(this::toDto).toList();
    }

    private NewDeviceStateResponse toDto(DeviceState state) {
        Device device = state.getDevice();

        return NewDeviceStateResponse.builder()
                .deviceId(device.getId())
                .deviceExternalId(device.getExternalId())
                .name(device.getName())
                .type(device.getType())
                .status(device.getStatus())
                .updatedAt(state.getUpdatedAt())
                .hasError(state.getHasError())
                .errorMessage(state.getErrorMessage())
                .deviceStatePayload(state.getDeviceStatePayload())
                .build();
    }
}
