package ru.tbank.practicum.dto.internal;

import jakarta.validation.constraints.NotNull;
import ru.tbank.practicum.validation.LessThan;

import java.time.Instant;

@LessThan(less = "openingTime", more = "closingTime")
public record DtoTimeBlind(
        @NotNull(message = "openingTime не задан") Instant openingTime,
        @NotNull(message = "closingTime не задан") Instant closingTime) {}
