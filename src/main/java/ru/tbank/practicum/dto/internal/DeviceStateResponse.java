package ru.tbank.practicum.dto.internal;

import java.time.OffsetDateTime;
import ru.tbank.practicum.entity.statePayload.DeviceStatePayload;

public record DeviceStateResponse(
        Long id,
        DeviceStatePayload deviceStatePayload,
        Boolean hasError,
        String errorMessage,
        OffsetDateTime updatedAt) {}
