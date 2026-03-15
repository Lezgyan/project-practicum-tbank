package ru.tbank.practicum.dto.internal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DtoCoordinate(
        @Min(-90) @Max(90) @NotNull(message = "lat не задан")
        Double lat,

        @Min(0) @Max(180) @NotNull(message = "lon не задан") Double lon) {}
