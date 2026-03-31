package ru.tbank.practicum.entity.eventPayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceHeartbeatEventPayload implements DeviceEventPayload {
    private long timestamp;
}
