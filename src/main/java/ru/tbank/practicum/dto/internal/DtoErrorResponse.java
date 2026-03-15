package ru.tbank.practicum.dto.internal;

import java.time.Instant;
import java.util.Map;

public record DtoErrorResponse(
        Instant timestamp, int status, String error, String message, Map<String, String> fields) {}
