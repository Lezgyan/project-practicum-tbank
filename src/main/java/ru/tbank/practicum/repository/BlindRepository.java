package ru.tbank.practicum.repository;

import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class BlindRepository {

    public void openBlind(Long id) {
        log.info("Open the blind");
    }

    public void closeBlind(Long id) {
        log.info("Close blind");
    }

    public void setOpeningAndClosingTime(Long id, Instant openingTime, Instant closingTime) {
        log.info("Set opening and closing time ");
    }
}
