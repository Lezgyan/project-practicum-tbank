package ru.tbank.practicum.dto.external;


public record Weather(
        int id,
        String main,
        String description,
        String icon
) {
}
