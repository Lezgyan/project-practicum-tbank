package ru.tbank.practicum.dto.internal;

import jakarta.validation.constraints.NotNull;

public record DtoTemperature(
        @NotNull(message = "degree не задан") Double degree) {}
