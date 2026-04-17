package ru.tbank.practicum.entity.commandPayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tbank.practicum.enums.AutoDeviceCommand;
import ru.tbank.practicum.enums.BlindsState;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlindsCommandPayload implements DeviceCommandPayload {
    private BlindsState command;
    private AutoDeviceCommand reason;
}
