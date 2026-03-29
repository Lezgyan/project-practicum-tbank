package ru.tbank.practicum.entity.statePayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RadiatorStatePayload implements DeviceStatePayload {
    private Double temperature;
}
