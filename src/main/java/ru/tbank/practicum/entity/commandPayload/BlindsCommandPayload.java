package ru.tbank.practicum.entity.commandPayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlindsCommandPayload implements DeviceCommandPayload {
    private Boolean open;
    private String reason;
}
