package ru.tbank.practicum.entity.commandPayload;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "kind")
@JsonSubTypes({
    @JsonSubTypes.Type(value = BlindsCommandPayload.class, name = "BLINDS_COMMAND"),
    @JsonSubTypes.Type(value = RadiatorCommandPayload.class, name = "RADIATOR_COMMAND")
})
public sealed interface DeviceCommandPayload permits BlindsCommandPayload, RadiatorCommandPayload {}
