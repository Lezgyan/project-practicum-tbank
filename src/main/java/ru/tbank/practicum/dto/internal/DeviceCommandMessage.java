package ru.tbank.practicum.dto.internal;

import java.time.Instant;
import ru.tbank.practicum.entity.commandPayload.DeviceCommandPayload;
import ru.tbank.practicum.enums.DeviceType;

public record DeviceCommandMessage(
        long deviceId,
        String externalId,
        DeviceType deviceType,
        String commandType,
        DeviceCommandPayload payload,
        Instant createdAt) {}
