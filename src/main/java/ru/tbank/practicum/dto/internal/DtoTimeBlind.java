package ru.tbank.practicum.dto.internal;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import ru.tbank.practicum.validation.ValidTimeOpeningAndClosingBlind;

@ValidTimeOpeningAndClosingBlind
public record DtoTimeBlind(
        @NotNull(message = "openingTime не задан") Instant openingTime,
        @NotNull(message = "closingTime не задан") Instant closingTime) {}
