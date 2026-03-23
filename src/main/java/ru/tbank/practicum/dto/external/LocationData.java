package ru.tbank.practicum.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LocationData(
        @JsonProperty("type") Integer type,
        @JsonProperty("id") Integer id,
        @JsonProperty("message") String message,
        @JsonProperty("country") String countryCode,
        @JsonProperty("sunrise") Long sunrise,
        @JsonProperty("sunset") Long sunset) {}
