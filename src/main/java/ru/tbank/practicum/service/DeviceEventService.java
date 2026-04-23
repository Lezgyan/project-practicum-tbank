package ru.tbank.practicum.service;

import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.practicum.avro.BlindsStateChangedEventPayloadAvro;
import ru.tbank.practicum.avro.DeviceErrorEventPayloadAvro;
import ru.tbank.practicum.avro.DeviceEventMessageAvro;
import ru.tbank.practicum.avro.DeviceHeartbeatEventPayloadAvro;
import ru.tbank.practicum.avro.DeviceRecoveredEventPayloadAvro;
import ru.tbank.practicum.avro.EventTypeAvro;
import ru.tbank.practicum.avro.RadiatorStateChangedEventPayloadAvro;
import ru.tbank.practicum.entity.Device;
import ru.tbank.practicum.entity.DeviceState;
import ru.tbank.practicum.entity.statePayload.BlindsStatePayload;
import ru.tbank.practicum.entity.statePayload.RadiatorStatePayload;
import ru.tbank.practicum.enums.DeviceStatus;
import ru.tbank.practicum.repository.DeviceRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceEventService {

    private final DeviceRepository deviceRepository;

    @Transactional
    public void handle(DeviceEventMessageAvro event) {
        Device device = deviceRepository.findById(event.getDeviceId()).orElse(null);

        if (device == null) {
            log.warn(
                    "Событие пропущено: устройство не найдено. deviceId={}, eventType={}",
                    event.getDeviceId(),
                    event.getEventType());
            return;
        }

        DeviceState state = device.getDeviceState();

        EventTypeAvro eventType = event.getEventType();
        Object payload = event.getPayload();

        switch (eventType) {
            case BLINDS_STATE_CHANGED -> handleBlindsStateChanged(device, state, payload);
            case RADIATOR_STATE_CHANGED -> handleRadiatorStateChanged(device, state, payload);
            case ERROR -> handleDeviceError(device, state, payload);
            case RECOVERED -> handleDeviceRecovered(device, state, payload);
            case HEARTBEAT -> handleDeviceHeartbeat(device, state, payload);
            default -> {
                log.warn(
                        "Событие пропущено: неподдерживаемый eventType={}, deviceId={}",
                        eventType,
                        event.getDeviceId());
                return;
            }
        }

        deviceRepository.save(device);
    }

    private void handleBlindsStateChanged(Device device, DeviceState state, Object payload) {
        if (!(payload instanceof BlindsStateChangedEventPayloadAvro blindsPayload)) {
            throw new IllegalArgumentException("Invalid payload for BLINDS_STATE_CHANGED: " + payload);
        }

        state.setHasError(false);
        state.setErrorMessage(null);

        state.setDeviceStatePayload(new BlindsStatePayload(blindsPayload.getOpen()));

        state.setUpdatedAt(OffsetDateTime.now());
        device.setStatus(DeviceStatus.ACTIVE);
    }

    private void handleRadiatorStateChanged(Device device, DeviceState state, Object payload) {
        if (!(payload instanceof RadiatorStateChangedEventPayloadAvro radiatorPayload)) {
            throw new IllegalArgumentException("Invalid payload for RADIATOR_STATE_CHANGED: " + payload);
        }

        state.setHasError(false);
        state.setErrorMessage(null);

        state.setDeviceStatePayload(new RadiatorStatePayload(radiatorPayload.getTemperature()));

        device.setStatus(DeviceStatus.ACTIVE);
        state.setUpdatedAt(OffsetDateTime.now());
    }

    private void handleDeviceError(Device device, DeviceState state, Object payload) {
        if (!(payload instanceof DeviceErrorEventPayloadAvro errorPayload)) {
            throw new IllegalArgumentException("Invalid payload for DEVICE_ERROR: " + payload);
        }

        state.setHasError(true);
        state.setErrorMessage(errorPayload.getMessage());

        device.setStatus(DeviceStatus.BROKEN);
        state.setUpdatedAt(OffsetDateTime.now());
    }

    private void handleDeviceRecovered(Device device, DeviceState state, Object payload) {
        if (!(payload instanceof DeviceRecoveredEventPayloadAvro)) {
            throw new IllegalArgumentException("Invalid payload for DEVICE_RECOVERED: " + payload);
        }

        state.setHasError(false);
        state.setErrorMessage(null);

        device.setStatus(DeviceStatus.ACTIVE);
        state.setUpdatedAt(OffsetDateTime.now());
    }

    private void handleDeviceHeartbeat(Device device, DeviceState state, Object payload) {
        if (!(payload instanceof DeviceHeartbeatEventPayloadAvro)) {
            throw new IllegalArgumentException("Invalid payload for DEVICE_HEARTBEAT: " + payload);
        }

        device.setStatus(DeviceStatus.ACTIVE);
        state.setUpdatedAt(OffsetDateTime.now());
    }
}
