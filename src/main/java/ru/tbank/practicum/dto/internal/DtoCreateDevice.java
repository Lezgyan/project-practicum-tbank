package ru.tbank.practicum.dto.internal;

import jakarta.validation.constraints.NotNull;
import ru.tbank.practicum.enums.DeviceStatus;
import ru.tbank.practicum.enums.DeviceType;

public record DtoCreateDevice(
        @NotNull String name,
        @NotNull DeviceType type,
        @NotNull DeviceStatus status) {}
