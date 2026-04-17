package ru.tbank.practicum.mapper;

import java.time.Instant;
import org.springframework.stereotype.Component;
import ru.tbank.practicum.avro.BlindsCommandPayloadAvro;
import ru.tbank.practicum.avro.BlindsStateAvro;
import ru.tbank.practicum.avro.CommandTypeAvro;
import ru.tbank.practicum.avro.DeviceCommandMessageAvro;
import ru.tbank.practicum.avro.DeviceTypeAvro;
import ru.tbank.practicum.avro.RadiatorCommandPayloadAvro;
import ru.tbank.practicum.entity.Device;
import ru.tbank.practicum.entity.commandPayload.BlindsCommandPayload;
import ru.tbank.practicum.entity.commandPayload.DeviceCommandPayload;
import ru.tbank.practicum.entity.commandPayload.RadiatorCommandPayload;
import ru.tbank.practicum.enums.BlindsState;
import ru.tbank.practicum.enums.CommandType;
import ru.tbank.practicum.enums.DeviceType;

@Component
public class DeviceCommandMessageMapper {

    public DeviceCommandMessageAvro toAvroMessage(
            Device device, CommandType commandType, DeviceCommandPayload payload) {
        Object avroPayload = mapPayload(payload);

        return DeviceCommandMessageAvro.newBuilder()
                .setDeviceId(device.getId())
                .setExternalId(String.valueOf(device.getExternalId()))
                .setDeviceType(mapDeviceType(device.getType()))
                .setCommandType(mapCommandType(commandType))
                .setPayload(avroPayload)
                .setCreatedAtEpochMillis(Instant.now().toEpochMilli())
                .build();
    }

    private Object mapPayload(DeviceCommandPayload payload) {
        if (payload instanceof BlindsCommandPayload blinds) {
            return BlindsCommandPayloadAvro.newBuilder()
                    .setState(mapBlindsState(blinds.getCommand()))
                    .setReason(blinds.getReason().name())
                    .build();
        }

        if (payload instanceof RadiatorCommandPayload radiator) {
            return RadiatorCommandPayloadAvro.newBuilder()
                    .setTargetTemperature(radiator.getTargetTemperature())
                    .setReason(radiator.getReason().name())
                    .build();
        }

        throw new IllegalArgumentException(
                "Unsupported payload type: " + payload.getClass().getName());
    }

    private DeviceTypeAvro mapDeviceType(DeviceType type) {
        return switch (type) {
            case BLINDS -> DeviceTypeAvro.BLINDS;
            case RADIATOR -> DeviceTypeAvro.RADIATOR;
        };
    }

    private CommandTypeAvro mapCommandType(CommandType type) {
        return switch (type) {
            case OPEN -> CommandTypeAvro.OPEN;
            case CLOSE -> CommandTypeAvro.CLOSE;
            case SET_TEMPERATURE -> CommandTypeAvro.SET_TEMPERATURE;
        };
    }

    private BlindsStateAvro mapBlindsState(BlindsState state) {
        return switch (state) {
            case OPEN -> BlindsStateAvro.OPEN;
            case CLOSED -> BlindsStateAvro.CLOSE;
        };
    }
}
