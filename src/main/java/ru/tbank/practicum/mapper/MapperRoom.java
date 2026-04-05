package ru.tbank.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.tbank.practicum.dto.internal.DtoCreateRoom;
import ru.tbank.practicum.dto.internal.DtoCreateRoomResponse;
import ru.tbank.practicum.entity.Room;

@Mapper(componentModel = "spring")
public interface MapperRoom {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "devices", ignore = true)
    @Mapping(target = "weather", ignore = true)
    Room mapToEntityRoom(DtoCreateRoom dtoCreateRoom);

    @Mapping(target = "roomId", source = "id")
    DtoCreateRoomResponse mapToCreateRoomResponse(Room room);
}
