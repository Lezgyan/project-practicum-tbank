package ru.tbank.practicum.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MainWeatherInfo(
        @JsonProperty("temp") Double temperature,
        @JsonProperty("feels_like") Double feelsLike,
        @JsonProperty("temp_min") Double minTemperature,
        @JsonProperty("temp_max") Double maxTemperature,
        @JsonProperty("pressure") Integer pressure,
        @JsonProperty("humidity") Integer humidity,
        @JsonProperty("sea_level") Integer seaLevel,
        @JsonProperty("grnd_level") Integer groundLevel) {}
