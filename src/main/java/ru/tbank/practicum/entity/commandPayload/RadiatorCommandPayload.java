package ru.tbank.practicum.entity.commandPayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tbank.practicum.enums.AutoDeviceCommand;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class RadiatorCommandPayload implements DeviceCommandPayload {
    private double targetTemperature;
    private AutoDeviceCommand reason;
}
