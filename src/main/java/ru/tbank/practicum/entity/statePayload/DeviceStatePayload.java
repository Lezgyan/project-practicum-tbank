package ru.tbank.practicum.entity.statePayload;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "kind")
@JsonSubTypes({
    @JsonSubTypes.Type(value = BlindsStatePayload.class, name = "BLINDS"),
    @JsonSubTypes.Type(value = RadiatorStatePayload.class, name = "RADIATOR")
})
public interface DeviceStatePayload {}
