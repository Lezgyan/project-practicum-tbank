package ru.tbank.practicum.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tbank.practicum.avro.CommandTypeAvro;
import ru.tbank.practicum.avro.DeviceCommandMessageAvro;
import ru.tbank.practicum.avro.DeviceTypeAvro;
import ru.tbank.practicum.config.KafkaTopicProperties;
import ru.tbank.practicum.entity.Device;
import ru.tbank.practicum.entity.DeviceCommandLog;
import ru.tbank.practicum.entity.DeviceState;
import ru.tbank.practicum.entity.commandPayload.RadiatorCommandPayload;
import ru.tbank.practicum.enums.AutoDeviceCommand;
import ru.tbank.practicum.enums.CommandStatus;
import ru.tbank.practicum.enums.CommandType;
import ru.tbank.practicum.enums.DeviceStatus;
import ru.tbank.practicum.mapper.DeviceCommandMessageMapper;
import ru.tbank.practicum.repository.DeviceCommandLogRepository;
import ru.tbank.practicum.repository.DeviceRepository;
import ru.tbank.practicum.repository.DeviceStateRepository;

@ExtendWith(MockitoExtension.class)
class DeviceCommandServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private DeviceStateRepository deviceStateRepository;

    @Mock
    private DeviceCommandLogRepository deviceCommandLogRepository;

    @Mock
    private KafkaTopicProperties kafkaTopicProperties;

    @Mock
    private DeviceCommandMessageMapper mapper;

    @Mock
    private DeviceCommandProducer producer;

    @InjectMocks
    private DeviceCommandService service;

    @Test
    void setRadiatorTemperature_success_savesLogAndMarksItSent() {
        Device device = new Device();
        device.setId(1L);
        device.setExternalId(UUID.randomUUID());
        device.setStatus(DeviceStatus.ACTIVE);

        DeviceState state = new DeviceState();
        state.setHasError(false);

        DeviceCommandLog savedLog = DeviceCommandLog.builder()
                .device(device)
                .commandType(CommandType.SET_TEMPERATURE)
                .status(CommandStatus.NEW)
                .build();

        DeviceCommandMessageAvro message = DeviceCommandMessageAvro.newBuilder()
                .setDeviceId(1L)
                .setDeviceType(DeviceTypeAvro.BLINDS)
                .setExternalId("test")
                .setCommandType(CommandTypeAvro.CLOSE)
                .setCreatedAtEpochMillis(21352L)
                .build();

        when(deviceStateRepository.findById(1L)).thenReturn(Optional.of(state));
        when(kafkaTopicProperties.radiatorCommands()).thenReturn("radiator-commands");
        when(deviceCommandLogRepository.save(any(DeviceCommandLog.class))).thenReturn(savedLog);
        when(mapper.toAvroMessage(eq(device), eq(CommandType.SET_TEMPERATURE), any(RadiatorCommandPayload.class)))
                .thenReturn(message);

        service.setRadiatorTemperature(device, 22.2, AutoDeviceCommand.AUTO_COLD_WEATHER);

        verify(producer).send("radiator-commands", device.getExternalId().toString(), message);

        ArgumentCaptor<DeviceCommandLog> captor = ArgumentCaptor.forClass(DeviceCommandLog.class);
        verify(deviceCommandLogRepository, atLeast(2)).save(captor.capture());

        DeviceCommandLog lastSaved = captor.getAllValues().getLast();
        assertEquals(CommandStatus.SENT, lastSaved.getStatus());
    }

    @Test
    void setRadiatorTemperature_whenProducerFails_marksLogFailedAndThrows() {
        Device device = new Device();
        device.setId(2L);
        device.setExternalId(UUID.randomUUID());
        device.setStatus(DeviceStatus.ACTIVE);

        DeviceState state = new DeviceState();
        state.setHasError(false);

        DeviceCommandLog savedLog = DeviceCommandLog.builder()
                .device(device)
                .commandType(CommandType.SET_TEMPERATURE)
                .status(CommandStatus.NEW)
                .build();

        DeviceCommandMessageAvro message = DeviceCommandMessageAvro.newBuilder()
                .setDeviceId(2L)
                .setDeviceType(DeviceTypeAvro.BLINDS)
                .setExternalId("test")
                .setCommandType(CommandTypeAvro.CLOSE)
                .setCreatedAtEpochMillis(15L)
                .build();

        when(deviceStateRepository.findById(2L)).thenReturn(Optional.of(state));
        when(kafkaTopicProperties.radiatorCommands()).thenReturn("radiator-commands");
        when(deviceCommandLogRepository.save(any(DeviceCommandLog.class))).thenReturn(savedLog);
        when(mapper.toAvroMessage(eq(device), eq(CommandType.SET_TEMPERATURE), any(RadiatorCommandPayload.class)))
                .thenReturn(message);

        doThrow(new RuntimeException("Kafka unavailable"))
                .when(producer)
                .send("radiator-commands", device.getExternalId().toString(), message);

        assertThrows(
                RuntimeException.class,
                () -> service.setRadiatorTemperature(device, 19.0, AutoDeviceCommand.AUTO_HOT_WEATHER));

        ArgumentCaptor<DeviceCommandLog> captor = ArgumentCaptor.forClass(DeviceCommandLog.class);
        verify(deviceCommandLogRepository, atLeast(2)).save(captor.capture());

        DeviceCommandLog lastSaved = captor.getAllValues().getLast();
        assertEquals(CommandStatus.FAILED, lastSaved.getStatus());
    }

    @Test
    void setRadiatorTemperature_whenDeviceNotActive_throwsException() {
        Device device = new Device();
        device.setId(3L);
        device.setStatus(DeviceStatus.INACTIVE);

        assertThrows(
                IllegalStateException.class,
                () -> service.setRadiatorTemperature(device, 21.1, AutoDeviceCommand.HANDLE_CHANGE));
    }
}
