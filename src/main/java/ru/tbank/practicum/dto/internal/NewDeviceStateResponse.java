package ru.tbank.practicum.dto.internal;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import ru.tbank.practicum.entity.statePayload.DeviceStatePayload;
import ru.tbank.practicum.enums.DeviceStatus;
import ru.tbank.practicum.enums.DeviceType;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewDeviceStateResponse {

    Long deviceId;
    UUID deviceExternalId;
    String name;
    DeviceType type;
    DeviceStatus status;

    OffsetDateTime updatedAt;
    Boolean hasError;
    String errorMessage;

    DeviceStatePayload deviceStatePayload;
}
