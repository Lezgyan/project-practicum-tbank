package ru.tbank.practicum.dto.internal;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record DtoTimeBlind(
        @NotNull(message = "time не задан") Instant time) {}
