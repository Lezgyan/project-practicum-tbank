package ru.tbank.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.tbank.practicum.dto.internal.DtoCreateDevice;
import ru.tbank.practicum.dto.internal.DtoCreateDeviceResponse;
import ru.tbank.practicum.entity.Device;

@Mapper(componentModel = "spring")
public interface MapperDevice {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "deviceState", ignore = true)
    @Mapping(target = "settings", ignore = true)
    Device mapToDeviceEntity(DtoCreateDevice dtoCreateDevice);

    @Mapping(target = "deviceId", source = "id")
    DtoCreateDeviceResponse mapToCreateDeviceResponse(Device device);
}
