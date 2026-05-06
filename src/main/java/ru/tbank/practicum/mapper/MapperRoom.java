package ru.tbank.practicum.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.tbank.practicum.dto.internal.DeviceShortDto;
import ru.tbank.practicum.dto.internal.DtoCreateRoom;
import ru.tbank.practicum.dto.internal.DtoCreateRoomResponse;
import ru.tbank.practicum.dto.internal.RoomResponseDto;
import ru.tbank.practicum.dto.internal.WeatherResponseDto;
import ru.tbank.practicum.entity.Device;
import ru.tbank.practicum.entity.Room;
import ru.tbank.practicum.entity.WeatherMeasurement;

@Mapper(componentModel = "spring")
public interface MapperRoom {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "devices", ignore = true)
    @Mapping(target = "weather", ignore = true)
    Room mapToEntityRoom(DtoCreateRoom dtoCreateRoom);

    @Mapping(target = "roomId", source = "id")
    DtoCreateRoomResponse mapToCreateRoomResponse(Room room);

    RoomResponseDto toDto(Room room);

    List<RoomResponseDto> toDtoList(List<Room> rooms);

    @Mapping(target = "type", source = "type", qualifiedByName = "enumToString")
    @Mapping(target = "status", source = "status", qualifiedByName = "enumToString")
    //    @Mapping(target = "deviceState", source = "deviceState", qualifiedByName = "mapDeviceState")
    DeviceShortDto toDeviceShortDto(Device device);

    List<DeviceShortDto> toDeviceShortDtoList(List<Device> devices);

    WeatherResponseDto toWeatherResponseDto(WeatherMeasurement weather);

    @Named("enumToString")
    static String enumToString(Enum<?> value) {
        return value == null ? null : value.name();
    }

    //    @Named("mapDeviceState")
    //    static DeviceStateResponse mapDeviceState(DeviceState deviceState) {
    //        return new DeviceStateResponse(
    //                deviceState.getId(),
    //                deviceState.getDeviceStatePayload(),
    //                deviceState.getHasError(),
    //                deviceState.getErrorMessage(),
    //                deviceState.getUpdatedAt());
    //    }
}
