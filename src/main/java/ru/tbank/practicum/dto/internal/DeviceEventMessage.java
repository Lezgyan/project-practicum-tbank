package ru.tbank.practicum.dto.internal;

import java.time.Instant;
import ru.tbank.practicum.entity.eventPayload.DeviceEventPayload;
import ru.tbank.practicum.enums.EventType;

public record DeviceEventMessage(
        String externalId,
        long deviceID,
        EventType eventType,
        DeviceEventPayload payload,
        boolean hasError,
        String errorMessage,
        Instant createdAt) {}
