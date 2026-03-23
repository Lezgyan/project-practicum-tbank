package ru.tbank.practicum.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WindInfo(
        @JsonProperty("speed") Double speed,
        @JsonProperty("deg") Integer directionDegrees,
        @JsonProperty("gust") Double gust) {}
