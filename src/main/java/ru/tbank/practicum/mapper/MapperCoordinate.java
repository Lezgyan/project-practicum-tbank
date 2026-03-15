package ru.tbank.practicum.mapper;

import org.mapstruct.Mapper;
import ru.tbank.practicum.dto.internal.DtoCoordinate;
import ru.tbank.practicum.entity.EntityCoordinate;

@Mapper(componentModel = "spring")
public interface MapperCoordinate {
    EntityCoordinate mapToEntityCoordinate(DtoCoordinate dtoCoordinate);

    DtoCoordinate mapToDtoCoordinate(EntityCoordinate dtoCoordinate);
}
