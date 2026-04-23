package ru.tbank.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.tbank.practicum.avro.DeviceEventMessageAvro;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceStatusConsumer {

    private final DeviceEventService deviceEventService;

    @KafkaListener(topics = "${app.kafka.topics.device-events}", groupId = "smart-house-device-events")
    public void listen(DeviceEventMessageAvro event) {
        log.info("УРА!!! МЫ ПОЛУЧИЛИ ОТВЕТ ОТ УСТРОЙСТВ: {}", event);

        deviceEventService.handle(event);
    }
}
