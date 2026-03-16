package ru.tbank.practicum.entity;

import java.time.Instant;

public record EntityTimeBlind(Instant openingTime, Instant closingTime) {}
