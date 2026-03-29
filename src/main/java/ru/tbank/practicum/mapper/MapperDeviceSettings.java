package ru.tbank.practicum.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.tbank.practicum.dto.internal.UpdateDeviceSettingsRequest;
import ru.tbank.practicum.dto.internal.UpdateDeviceSettingsResponse;
import ru.tbank.practicum.entity.DeviceSettings;

@Mapper(componentModel = "spring")
public interface MapperDeviceSettings {
    UpdateDeviceSettingsRequest toUpdateDeviceSettingsRequest(DeviceSettings deviceSettings);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "device", ignore = true)
    DeviceSettings toDeviceSettings(UpdateDeviceSettingsRequest updateDeviceSettingsRequest);

    UpdateDeviceSettingsResponse toDeviceSettingsResponse(DeviceSettings deviceSettings);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDeviceSettingsFromRequest(DeviceSettings request, @MappingTarget DeviceSettings deviceSettings);
}
