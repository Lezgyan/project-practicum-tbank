package ru.tbank.practicum.service;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import ru.tbank.practicum.avro.CommandTypeAvro;
import ru.tbank.practicum.avro.DeviceCommandMessageAvro;
import ru.tbank.practicum.avro.DeviceTypeAvro;

@ExtendWith(MockitoExtension.class)
class DeviceCommandProducerTest {

    @Mock
    private KafkaTemplate<String, DeviceCommandMessageAvro> kafkaTemplate;

    @InjectMocks
    private DeviceCommandProducer producer;

    @Test
    void send_delegatesToKafkaTemplate() {
        DeviceCommandMessageAvro event = DeviceCommandMessageAvro.newBuilder()
                .setDeviceId(2234L)
                .setDeviceType(DeviceTypeAvro.BLINDS)
                .setExternalId("test")
                .setCommandType(CommandTypeAvro.CLOSE)
                .setCreatedAtEpochMillis(2134216L)
                .build();

        producer.send("commands-topic", "ext-1", event);

        verify(kafkaTemplate).send("commands-topic", "ext-1", event);
    }
}
