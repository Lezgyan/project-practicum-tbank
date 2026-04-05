package ru.tbank.practicum.entity.eventPayload;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "kind")
@JsonSubTypes({
    @JsonSubTypes.Type(value = BlindsStateChangedEventPayload.class, name = "BLINDS_STATE_CHANGED"),
    @JsonSubTypes.Type(value = RadiatorStateChangedEventPayload.class, name = "RADIATOR_STATE_CHANGED"),
    @JsonSubTypes.Type(value = DeviceErrorEventPayload.class, name = "DEVICE_ERROR"),
    @JsonSubTypes.Type(value = DeviceRecoveredEventPayload.class, name = "DEVICE_RECOVERED"),
    @JsonSubTypes.Type(value = DeviceHeartbeatEventPayload.class, name = "DEVICE_HEARTBEAT")
})
public interface DeviceEventPayload {}
