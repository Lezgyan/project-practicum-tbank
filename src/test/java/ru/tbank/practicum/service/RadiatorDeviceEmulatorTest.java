package ru.tbank.practicum.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import ru.tbank.practicum.avro.CommandTypeAvro;
import ru.tbank.practicum.avro.DeviceCommandMessageAvro;
import ru.tbank.practicum.avro.DeviceEventMessageAvro;
import ru.tbank.practicum.avro.DeviceTypeAvro;
import ru.tbank.practicum.avro.EventTypeAvro;
import ru.tbank.practicum.avro.RadiatorCommandPayloadAvro;
import ru.tbank.practicum.avro.RadiatorStateChangedEventPayloadAvro;
import ru.tbank.practicum.component.RadiatorDeviceEmulator;
import ru.tbank.practicum.config.KafkaTopicProperties;

@ExtendWith(MockitoExtension.class)
class RadiatorDeviceEmulatorTest {

    @Mock
    private KafkaTemplate<String, DeviceEventMessageAvro> kafkaTemplate;

    @Mock
    private KafkaTopicProperties kafkaTopicProperties;

    @InjectMocks
    private RadiatorDeviceEmulator emulator;

    @Test
    void handleBlindsCommand_validRadiatorPayload_sendsRadiatorStateChangedEvent() {
        when(kafkaTopicProperties.deviceEvents()).thenReturn("device-events");

        RadiatorCommandPayloadAvro payload = RadiatorCommandPayloadAvro.newBuilder()
                .setTargetTemperature(23.5)
                .setReason("AUTO_COLD_WEATHER")
                .build();

        DeviceCommandMessageAvro command = DeviceCommandMessageAvro.newBuilder()
                .setDeviceId(10L)
                .setExternalId("test")
                .setPayload(payload)
                .setCommandType(CommandTypeAvro.CLOSE)
                .setCreatedAtEpochMillis(213521L)
                .setDeviceType(DeviceTypeAvro.RADIATOR)
                .build();

        emulator.handleBlindsCommand(command);

        ArgumentCaptor<DeviceEventMessageAvro> captor = ArgumentCaptor.forClass(DeviceEventMessageAvro.class);

        verify(kafkaTemplate).send(eq("device-events"), eq("test"), captor.capture());

        DeviceEventMessageAvro event = captor.getValue();

        assertEquals(10L, event.getDeviceId());
        assertEquals("test", event.getExternalId());
        assertEquals(EventTypeAvro.RADIATOR_STATE_CHANGED, event.getEventType());

        RadiatorStateChangedEventPayloadAvro eventPayload =
                assertInstanceOf(RadiatorStateChangedEventPayloadAvro.class, event.getPayload());

        assertEquals(23.5, eventPayload.getTemperature());
    }

    @Test
    void handleBlindsCommand_invalidPayload_doesNotSendAnything() {
        DeviceCommandMessageAvro command = DeviceCommandMessageAvro.newBuilder()
                .setDeviceId(11L)
                .setExternalId("test")
                .setCommandType(CommandTypeAvro.OPEN)
                .setDeviceType(DeviceTypeAvro.RADIATOR)
                .setCreatedAtEpochMillis(121950332L)
                .setPayload(null)
                .build();

        emulator.handleBlindsCommand(command);

        verify(kafkaTemplate, never()).send(eq("device-events"), eq("test"), any());
    }
}
