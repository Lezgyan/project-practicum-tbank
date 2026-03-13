package ru.tbank.practicum.dto.external;

public record LocationData(
        int type,
        int id,
        String country,
        long sunrise,
        long sunset
) {
}
