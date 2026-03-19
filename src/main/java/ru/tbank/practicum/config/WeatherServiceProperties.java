package ru.tbank.practicum.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.weather-service")
public record WeatherServiceProperties(
        String baseUrl, String currentWeatherEndpoint, Cred cred, Coordinate coordinate, Scheduler scheduler) {
    public record Cred(String token) {}

    public record Coordinate(Double lon, Double lat) {}

    public record Scheduler(Long rate) {}
}
