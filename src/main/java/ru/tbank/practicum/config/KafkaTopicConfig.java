package ru.tbank.practicum.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

    private final KafkaTopicProperties kafkaTopicProperties;

    @Bean
    public NewTopic blindsCommandsTopic() {
        return new NewTopic(kafkaTopicProperties.blindsCommands(), 1, (short) 1);
    }

    @Bean
    public NewTopic radiatorCommandsTopic() {
        return new NewTopic(kafkaTopicProperties.radiatorCommands(), 1, (short) 1);
    }

    @Bean
    public NewTopic deviceEventsTopic() {
        return new NewTopic(kafkaTopicProperties.deviceEvents(), 1, (short) 1);
    }
}
