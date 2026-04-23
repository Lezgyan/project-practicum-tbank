package ru.tbank.practicum.component;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.tbank.practicum.avro.BlindsStateChangedEventPayloadAvro;
import ru.tbank.practicum.avro.CommandTypeAvro;
import ru.tbank.practicum.avro.DeviceCommandMessageAvro;
import ru.tbank.practicum.avro.DeviceEventMessageAvro;
import ru.tbank.practicum.avro.EventTypeAvro;
import ru.tbank.practicum.config.KafkaTopicProperties;

@Component
@Slf4j
@RequiredArgsConstructor
public class BlindsDeviceEmulator {
    private final KafkaTemplate<String, DeviceEventMessageAvro> kafkaTemplate;
    private final KafkaTopicProperties kafkaTopicProperties;

    @KafkaListener(topics = "${app.kafka.topics.blinds-commands}", groupId = "blinds-emulator")
    public void handleBlindsCommand(DeviceCommandMessageAvro command) {
        log.info(
                "ПОЛУЧИЛИ СООБЩЕНИЕ ИЗ ТОПИКА BLINDS-COMMAND: externalId={}, commandType={}",
                command.getExternalId(),
                command.getCommandType());

        boolean open = CommandTypeAvro.OPEN.equals(command.getCommandType());

        BlindsStateChangedEventPayloadAvro payload =
                BlindsStateChangedEventPayloadAvro.newBuilder().setOpen(open).build();

        DeviceEventMessageAvro event = DeviceEventMessageAvro.newBuilder()
                .setExternalId(command.getExternalId())
                .setEventType(EventTypeAvro.BLINDS_STATE_CHANGED)
                .setDeviceId(command.getDeviceId())
                .setPayload(payload)
                .setHasError(false)
                .setErrorMessage(null)
                .setCreatedAt(Instant.now().toEpochMilli())
                .build();

        kafkaTemplate.send(kafkaTopicProperties.deviceEvents(), command.getExternalId(), event);
    }
}
