package ru.tbank.practicum.dto.internal;

import jakarta.validation.constraints.NotNull;

public record DtoCreateRoom(
        @NotNull String name,
        @NotNull Double lon,
        @NotNull Double lat,
        @NotNull String timezone) {}
