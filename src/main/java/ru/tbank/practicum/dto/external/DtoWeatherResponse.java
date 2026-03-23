package ru.tbank.practicum.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record DtoWeatherResponse(
        @JsonProperty("coord") Coordinates coordinates,
        @JsonProperty("weather") List<WeatherCondition> weatherCondition,
        @JsonProperty("base") String base,
        @JsonProperty("main") MainWeatherInfo main,
        @JsonProperty("visibility") Integer visibility,
        @JsonProperty("wind") WindInfo windInfo,
        @JsonProperty("clouds") CloudsInfo cloudsInfo,
        @JsonProperty("rain") RainInfo rainInfo,
        @JsonProperty("snow") SnowInfo snowInfo,
        @JsonProperty("dt") Long timestamp,
        @JsonProperty("sys") LocationData systemInfo,
        @JsonProperty("timezone") Integer timezone,
        @JsonProperty("id") Long cityId,
        @JsonProperty("name") String cityName,
        @JsonProperty("cod") Integer cod) {}
