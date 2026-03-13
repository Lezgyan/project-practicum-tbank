package ru.tbank.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.tbank.practicum.dto.internal.DtoCoordinate;
import ru.tbank.practicum.entity.EntityCoordinate;

@Mapper(componentModel = "spring")
public interface MapperCoordinate {
    EntityCoordinate mapToEntity(DtoCoordinate dtoCoordinate);
    //DtoCoordinate mapToDto(EntityCoordinate dtoCoordinate);
}
