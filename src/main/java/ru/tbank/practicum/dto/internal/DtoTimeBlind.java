package ru.tbank.practicum.dto.internal;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import ru.tbank.practicum.validation.ValidTimeOpeningAndClosingBlind;

@ValidTimeOpeningAndClosingBlind
public record DtoTimeBlind(
        @NotNull(message = "time не задан") Instant openingTime,
        @NotNull(message = "time не задан") Instant closingTime) {}
