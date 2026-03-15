package ru.tbank.practicum.service;

import org.springframework.stereotype.Service;
import ru.tbank.practicum.dto.internal.DtoTimeBlind;
import ru.tbank.practicum.entity.EntityTimeBlind;
import ru.tbank.practicum.mapper.MapperTimeBlind;
import ru.tbank.practicum.repository.BlindRepository;

@Service
public class BlindService {

    private final BlindRepository blindRepository;

    private final MapperTimeBlind mapperTimeBlind;

    public BlindService(BlindRepository blindRepository, MapperTimeBlind mapperTimeBlind) {
        this.blindRepository = blindRepository;
        this.mapperTimeBlind = mapperTimeBlind;
    }

    public void openBlind() {
        blindRepository.openBlind();
    }

    public void closeBlind() {
        blindRepository.closeBlind();
    }

    public void setOpeningTime(DtoTimeBlind dtoTimeBlind) {
        EntityTimeBlind entityTimeBlind = mapperTimeBlind.mapToEntityTimeBlind(dtoTimeBlind);
        blindRepository.setOpeningTime(entityTimeBlind);
    }

    public void setClosingTime(DtoTimeBlind dtoTimeBlind) {
        EntityTimeBlind entityTimeBlind = mapperTimeBlind.mapToEntityTimeBlind(dtoTimeBlind);
        blindRepository.setClosingTime(entityTimeBlind);
    }
}
