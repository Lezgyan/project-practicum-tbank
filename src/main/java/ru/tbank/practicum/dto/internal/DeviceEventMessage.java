package ru.tbank.practicum.dto.internal;

import java.time.Instant;
import ru.tbank.practicum.entity.eventPayload.DeviceEventPayload;
import ru.tbank.practicum.enums.EventType;

public record DeviceEventMessage(
        String externalId,
        EventType eventType,
        DeviceEventPayload payload,
        Boolean hasError,
        String errorMessage,
        Instant createdAt) {}
