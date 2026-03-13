package ru.tbank.practicum.dto.external;

public record CommonData(
        double temp,
        double feels_like,
        double temp_min,
        double temp_max,
        int pressure,
        int humidity,
        int sea_level,
        int grnd_level
) {
}
