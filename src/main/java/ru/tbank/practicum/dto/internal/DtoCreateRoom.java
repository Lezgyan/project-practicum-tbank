package ru.tbank.practicum.dto.internal;

import jakarta.validation.constraints.NotNull;

public record DtoCreateRoom(
        @NotNull(message = "name is null") String name,
        @NotNull(message = "lon is null") Double lon,
        @NotNull(message = "lat is null") Double lat,
        @NotNull(message = "timezone is null") String timezone) {}
