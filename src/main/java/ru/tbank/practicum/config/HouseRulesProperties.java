package ru.tbank.practicum.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.house-rules")
public record HouseRulesProperties(String rate) {}
