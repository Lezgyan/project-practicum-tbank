package ru.tbank.practicum.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Coordinates(
        @JsonProperty("lon") Double lon,
        @JsonProperty("lat") Double lat) {}
