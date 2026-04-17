package ru.tbank.practicum.component;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.tbank.practicum.avro.DeviceCommandMessageAvro;
import ru.tbank.practicum.avro.DeviceEventMessageAvro;
import ru.tbank.practicum.avro.EventTypeAvro;
import ru.tbank.practicum.avro.RadiatorCommandPayloadAvro;
import ru.tbank.practicum.avro.RadiatorStateChangedEventPayloadAvro;
import ru.tbank.practicum.config.KafkaTopicProperties;

@Component
@Slf4j
@RequiredArgsConstructor
public class RadiatorDeviceEmulator {
    private final KafkaTemplate<String, DeviceEventMessageAvro> kafkaTemplate;
    private final KafkaTopicProperties kafkaTopicProperties;

    @KafkaListener(topics = "${app.kafka.topics.radiator-commands}", groupId = "radiator-emulator")
    public void handleBlindsCommand(DeviceCommandMessageAvro command) {
        log.info(
                "ПОЛУЧИЛИ СООБЩЕНИЕ ИЗ ТОПИКА RADIATOR-COMMAND: externalId={}, commandType={}",
                command.getExternalId(),
                command.getCommandType());

        double temperature = -300;
        if (command.getPayload() instanceof RadiatorCommandPayloadAvro) {
            temperature = ((RadiatorCommandPayloadAvro) command.getPayload()).getTargetTemperature();
        } else {
            throw new IllegalStateException("В КАФКУ В ТОПИК radiator-commands ПРИШЛО ЧТО ТО НЕ ТО");
        }

        RadiatorStateChangedEventPayloadAvro payload = RadiatorStateChangedEventPayloadAvro.newBuilder()
                .setTemperature(temperature)
                .build();

        DeviceEventMessageAvro event = DeviceEventMessageAvro.newBuilder()
                .setExternalId(command.getExternalId())
                .setDeviceId(command.getDeviceId())
                .setEventType(EventTypeAvro.RADIATOR_STATE_CHANGED)
                .setPayload(payload)
                .setHasError(false)
                .setErrorMessage(null)
                .setCreatedAt(Instant.now().toEpochMilli())
                .build();

        kafkaTemplate.send(
                kafkaTopicProperties.deviceEvents(), command.getExternalId().toString(), event);
    }
}
