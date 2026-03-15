package ru.tbank.practicum.mapper;

import org.mapstruct.Mapper;
import ru.tbank.practicum.dto.internal.DtoTimeBlind;
import ru.tbank.practicum.entity.EntityTimeBlind;

@Mapper(componentModel = "spring")
public interface MapperTimeBlind {
    DtoTimeBlind mapToDtoTimeBlind(EntityTimeBlind entityTimeBlind);

    EntityTimeBlind mapToEntityTimeBlind(DtoTimeBlind entityTimeBlind);
}
