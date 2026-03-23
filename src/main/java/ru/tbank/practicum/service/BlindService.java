package ru.tbank.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.dto.internal.DtoTimeBlind;
import ru.tbank.practicum.repository.BlindRepository;

@Service
@RequiredArgsConstructor
public class BlindService {

    private final BlindRepository blindRepository;

    public void openBlind(Long id) {
        blindRepository.openBlind(id);
    }

    public void closeBlind(Long id) {
        blindRepository.closeBlind(id);
    }

    public void setOpeningAndClosingTime(Long id, DtoTimeBlind dtoTimeBlind) {
        blindRepository.setOpeningAndClosingTime(id, dtoTimeBlind.openingTime(), dtoTimeBlind.closingTime());
    }
}
