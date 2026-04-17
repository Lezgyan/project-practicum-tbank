package ru.tbank.practicum.dto.internal;

import ru.tbank.practicum.enums.BlindsState;

public record UpdateBlindStateRequest(BlindsState command) {}
