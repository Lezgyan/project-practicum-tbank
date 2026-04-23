package ru.tbank.practicum.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka.topics")
public record KafkaTopicProperties(String blindsCommands, String radiatorCommands, String deviceEvents) {}
