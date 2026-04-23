package ru.tbank.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.avro.DeviceCommandMessageAvro;

@Service
@RequiredArgsConstructor
public class DeviceCommandProducer {

    private final KafkaTemplate<String, DeviceCommandMessageAvro> kafkaTemplate;

    public void send(String topic, String key, DeviceCommandMessageAvro event) {
        kafkaTemplate.send(topic, key, event);
    }
}
