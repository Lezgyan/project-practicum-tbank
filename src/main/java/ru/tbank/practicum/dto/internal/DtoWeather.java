package ru.tbank.practicum.dto.internal;

import jakarta.validation.constraints.NotNull;

public record DtoWeather(
        @NotNull(message = "cityId не задан") Long id,
        @NotNull(message = "temperature не задан") Double temperature,
        @NotNull(message = "description не задан") String description) {}
