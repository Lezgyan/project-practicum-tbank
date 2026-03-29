package ru.tbank.practicum.dto.internal;

import java.time.Instant;
import ru.tbank.practicum.entity.eventPayload.DeviceEventPayload;

public record DeviceEventMessage(
        String externalId,
        String eventType,
        DeviceEventPayload payload,
        Boolean hasError,
        String errorMessage,
        Instant createdAt) {}
