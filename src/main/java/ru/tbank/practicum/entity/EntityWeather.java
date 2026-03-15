package ru.tbank.practicum.entity;

public record EntityWeather(
        Long id,
        Double temperature,
        String description,
        Integer pressure,
        Double windSpeed,
        Double latCoordinate,
        Double lonCoordinate) {}
