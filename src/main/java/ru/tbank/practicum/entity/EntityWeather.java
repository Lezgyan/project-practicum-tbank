package ru.tbank.practicum.entity;

public record EntityWeather(
        Long id,
        double temperature,
        String description,
        int pressure,
        double windSpeed,
        double latCoordinate,
        double lonCoordinate
) {
}
