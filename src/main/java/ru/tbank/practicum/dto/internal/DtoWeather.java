package ru.tbank.practicum.dto.internal;

public record DtoWeather(
        Long id,
        Double temperature,
        String description
) {
}
