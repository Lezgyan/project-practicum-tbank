package ru.tbank.practicum.mapper;

import org.mapstruct.Mapper;
import ru.tbank.practicum.dto.internal.DtoTemperature;
import ru.tbank.practicum.entity.EntityTemperature;

@Mapper(componentModel = "spring")
public interface MapperTemperature {
    DtoTemperature mapToDtoTemperature(EntityTemperature entityTemperature);

    EntityTemperature mapToEntityTemperature(DtoTemperature dtoTemperature);
}
