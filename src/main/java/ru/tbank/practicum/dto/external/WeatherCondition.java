package ru.tbank.practicum.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherCondition(
        @JsonProperty("id") Integer id,
        @JsonProperty("main") String conditionGroup,
        @JsonProperty("description") String description,
        @JsonProperty("icon") String icon) {}
