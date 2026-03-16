package ru.tbank.practicum.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.tbank.practicum.entity.EntityTimeBlind;

@Slf4j
@Repository
public class BlindRepository {

    public void openBlind() {
        log.info("Open the blind");
    }

    public void closeBlind() {
        log.info("Close blind");
    }

    public void setOpeningAndClosingTime(EntityTimeBlind entityTimeBlind) {
        log.info("Set opening and closing time ");
    }
}
