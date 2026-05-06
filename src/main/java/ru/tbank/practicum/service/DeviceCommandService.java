package ru.tbank.practicum.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.practicum.avro.DeviceCommandMessageAvro;
import ru.tbank.practicum.config.KafkaTopicProperties;
import ru.tbank.practicum.entity.Device;
import ru.tbank.practicum.entity.DeviceCommandLog;
import ru.tbank.practicum.entity.DeviceState;
import ru.tbank.practicum.entity.commandPayload.BlindsCommandPayload;
import ru.tbank.practicum.entity.commandPayload.DeviceCommandPayload;
import ru.tbank.practicum.entity.commandPayload.RadiatorCommandPayload;
import ru.tbank.practicum.entity.statePayload.BlindsStatePayload;
import ru.tbank.practicum.enums.AutoDeviceCommand;
import ru.tbank.practicum.enums.BlindsState;
import ru.tbank.practicum.enums.CommandStatus;
import ru.tbank.practicum.enums.CommandType;
import ru.tbank.practicum.enums.DeviceStatus;
import ru.tbank.practicum.mapper.DeviceCommandMessageMapper;
import ru.tbank.practicum.repository.DeviceCommandLogRepository;
import ru.tbank.practicum.repository.DeviceRepository;
import ru.tbank.practicum.repository.DeviceStateRepository;

@Service
@Slf4j
@AllArgsConstructor
public class DeviceCommandService {
    private final DeviceRepository deviceRepository;
    private final DeviceStateRepository deviceStateRepository;
    private final DeviceCommandLogRepository deviceCommandLogRepository;

    private final KafkaTopicProperties kafkaTopicProperties;
    private final DeviceCommandMessageMapper mapper;

    private final DeviceCommandProducer producer;

    @Transactional
    public void setBlinds(Device device, BlindsState blindsState, AutoDeviceCommand command) {

        checkAvailable(device);

        boolean open = ((BlindsStatePayload) device.getDeviceState().getDeviceStatePayload()).isOpen();

        BlindsCommandPayload blindsCommandPayload = new BlindsCommandPayload(blindsState, command);

        CommandType commandType = (BlindsState.CLOSED).equals(blindsState) ? CommandType.CLOSE : CommandType.OPEN;

        sendCommand(device, commandType, blindsCommandPayload, kafkaTopicProperties.blindsCommands());
    }

    @Transactional
    public void setBlinds(Long deviceId, BlindsState blindsState, AutoDeviceCommand command) {
        Device device = getDevice(deviceId);
        setBlinds(device, blindsState, command);
    }

    @Transactional
    public void setRadiatorTemperature(Device device, Double targetTemperature, AutoDeviceCommand reason) {
        checkAvailable(device);

        RadiatorCommandPayload radiatorCommandPayload = new RadiatorCommandPayload(targetTemperature, reason);
        sendCommand(
                device, CommandType.SET_TEMPERATURE, radiatorCommandPayload, kafkaTopicProperties.radiatorCommands());
    }

    @Transactional
    public void setRadiatorTemperature(Long deviceId, Double targetTemperature, AutoDeviceCommand reason) {
        Device device = getDevice(deviceId);
        setRadiatorTemperature(device, targetTemperature, reason);
    }

    private void sendCommand(
            Device device, CommandType commandType, DeviceCommandPayload deviceCommandPayload, String topic) {
        DeviceCommandLog myLog = deviceCommandLogRepository.save(DeviceCommandLog.builder()
                .device(device)
                .commandType(commandType)
                .deviceCommandPayload(deviceCommandPayload)
                .status(CommandStatus.NEW)
                .build());

        try {
            DeviceCommandMessageAvro event = mapper.toAvroMessage(device, commandType, deviceCommandPayload);
            producer.send(topic, String.valueOf(device.getExternalId()), event);
            myLog.setStatus(CommandStatus.SENT);
        } catch (Exception e) {
            myLog.setStatus(CommandStatus.FAILED);
            log.error("sendCommand", e);
            throw new RuntimeException(e);
        } finally {
            deviceCommandLogRepository.save(myLog);
        }
    }

    private Device getDevice(Long id) {
        return deviceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Device not found: " + id));
    }

    private void checkAvailable(Device device) {
        if (device.getStatus() != DeviceStatus.ACTIVE) {
            throw new IllegalStateException("Device status is not ACTIVE: " + device.getStatus());
        }

        DeviceState state = deviceStateRepository.findById(device.getId()).orElse(null);
        if (state != null && state.getHasError()) {
            throw new IllegalStateException("Device has error: " + state.getErrorMessage());
        }
    }
}
