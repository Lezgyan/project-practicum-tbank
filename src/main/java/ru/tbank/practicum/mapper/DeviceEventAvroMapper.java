package ru.tbank.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.tbank.practicum.avro.BlindsStateChangedEventPayloadAvro;
import ru.tbank.practicum.avro.DeviceErrorEventPayloadAvro;
import ru.tbank.practicum.avro.DeviceEventMessageAvro;
import ru.tbank.practicum.avro.EventTypeAvro;
import ru.tbank.practicum.avro.RadiatorStateChangedEventPayloadAvro;
import ru.tbank.practicum.entity.eventPayload.BlindsStateChangedEventPayload;
import ru.tbank.practicum.entity.eventPayload.DeviceErrorEventPayload;
import ru.tbank.practicum.entity.eventPayload.DeviceEventPayload;
import ru.tbank.practicum.entity.eventPayload.RadiatorStateChangedEventPayload;

@Component
public class DeviceEventAvroMapper {

    public DeviceEventMessageAvro toAvro(DeviceEventMessageAvro message) {
        return DeviceEventMessageAvro.newBuilder()
                .setExternalId(message.getExternalId())
                .setEventType(EventTypeAvro.valueOf(message.getEventType().name()))
                .setPayload(mapPayload((DeviceEventPayload) message.getPayload()))
                .setHasError(message.getHasError())
                .setErrorMessage(message.getErrorMessage())
                .setCreatedAt(message.getCreatedAt())
                .build();
    }

    private Object mapPayload(DeviceEventPayload payload) {
        return switch (payload) {
            case null -> null;
            case BlindsStateChangedEventPayload p ->
                BlindsStateChangedEventPayloadAvro.newBuilder()
                        .setOpen(p.isOpen())
                        .build();
            case RadiatorStateChangedEventPayload p ->
                RadiatorStateChangedEventPayloadAvro.newBuilder()
                        .setTemperature(p.getTemperature())
                        .build();
            case DeviceErrorEventPayload p ->
                DeviceErrorEventPayloadAvro.newBuilder()
                        .setCode(p.getCode())
                        .setMessage(p.getMessage())
                        .build();
            default ->
                throw new IllegalArgumentException(
                        "Unsupported payload type: " + payload.getClass().getName());
        };
    }
}
