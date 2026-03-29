package ru.tbank.practicum.entity.commandPayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RadiatorCommandPayload implements DeviceCommandPayload {
    private Double targetTemperature;
    private String reason;
}
