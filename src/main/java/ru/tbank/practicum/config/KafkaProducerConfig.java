package ru.tbank.practicum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.tbank.practicum.avro.DeviceCommandMessageAvro;
import ru.tbank.practicum.avro.DeviceEventMessageAvro;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public KafkaTemplate<String, DeviceCommandMessageAvro> deviceCommandKafkaTemplate(
            ProducerFactory<String, DeviceCommandMessageAvro> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaTemplate<String, DeviceEventMessageAvro> deviceEventKafkaTemplate(
            ProducerFactory<String, DeviceEventMessageAvro> deviceEventProducerFactory) {
        return new KafkaTemplate<>(deviceEventProducerFactory);
    }
}
