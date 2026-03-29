package ru.tbank.practicum.dto.internal;

import jakarta.validation.constraints.NotNull;
import ru.tbank.practicum.enums.DeviceStatus;
import ru.tbank.practicum.enums.DeviceType;

public record DtoCreateDevice(
        @NotNull(message = "name is null") String name,
        @NotNull(message = "type is null") DeviceType type,
        @NotNull(message = "status is null") DeviceStatus status) {}
