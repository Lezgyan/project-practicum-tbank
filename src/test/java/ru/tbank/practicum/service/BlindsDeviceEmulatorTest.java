package ru.tbank.practicum.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import ru.tbank.practicum.avro.BlindsStateChangedEventPayloadAvro;
import ru.tbank.practicum.avro.CommandTypeAvro;
import ru.tbank.practicum.avro.DeviceCommandMessageAvro;
import ru.tbank.practicum.avro.DeviceEventMessageAvro;
import ru.tbank.practicum.avro.DeviceTypeAvro;
import ru.tbank.practicum.avro.EventTypeAvro;
import ru.tbank.practicum.component.BlindsDeviceEmulator;
import ru.tbank.practicum.config.KafkaTopicProperties;

@ExtendWith(MockitoExtension.class)
class BlindsDeviceEmulatorTest {

    @Mock
    private KafkaTemplate<String, DeviceEventMessageAvro> kafkaTemplate;

    @Mock
    private KafkaTopicProperties kafkaTopicProperties;

    @InjectMocks
    private BlindsDeviceEmulator emulator;

    @Test
    void handleBlindsCommand_openCommand_sendsBlindsStateChangedEvent() {
        when(kafkaTopicProperties.deviceEvents()).thenReturn("device-events");

        DeviceCommandMessageAvro command = DeviceCommandMessageAvro.newBuilder()
                .setDeviceId(1L)
                .setExternalId("test")
                .setCommandType(CommandTypeAvro.OPEN)
                .setDeviceType(DeviceTypeAvro.BLINDS)
                .setCreatedAtEpochMillis(124234L)
                .build();

        emulator.handleBlindsCommand(command);

        ArgumentCaptor<DeviceEventMessageAvro> captor = ArgumentCaptor.forClass(DeviceEventMessageAvro.class);

        verify(kafkaTemplate).send(eq("device-events"), eq("test"), captor.capture());

        DeviceEventMessageAvro event = captor.getValue();
        assertEquals(1L, event.getDeviceId());
        assertEquals("test", event.getExternalId());
        assertEquals(EventTypeAvro.BLINDS_STATE_CHANGED, event.getEventType());
        assertFalse(event.getHasError());

        BlindsStateChangedEventPayloadAvro payload =
                assertInstanceOf(BlindsStateChangedEventPayloadAvro.class, event.getPayload());

        assertTrue(payload.getOpen());
    }

    @Test
    void handleBlindsCommand_closeCommand_sendsClosedStateEvent() {
        when(kafkaTopicProperties.deviceEvents()).thenReturn("device-events");

        DeviceCommandMessageAvro command = DeviceCommandMessageAvro.newBuilder()
                .setDeviceId(2L)
                .setDeviceType(DeviceTypeAvro.BLINDS)
                .setExternalId("test")
                .setCommandType(CommandTypeAvro.CLOSE)
                .setCreatedAtEpochMillis(21352L)
                .build();

        emulator.handleBlindsCommand(command);

        ArgumentCaptor<DeviceEventMessageAvro> captor = ArgumentCaptor.forClass(DeviceEventMessageAvro.class);

        verify(kafkaTemplate).send(eq("device-events"), eq("test"), captor.capture());

        BlindsStateChangedEventPayloadAvro payload = assertInstanceOf(
                BlindsStateChangedEventPayloadAvro.class, captor.getValue().getPayload());

        assertFalse(payload.getOpen());
    }
}
