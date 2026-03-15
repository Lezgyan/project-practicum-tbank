package ru.tbank.practicum.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.tbank.practicum.entity.EntityTemperature;

@Slf4j
@Repository
public class BatteryRepository {

    public void upTemperature(EntityTemperature degree) {
        log.info("Up battery temperature");
    }

    public void downTemperature(EntityTemperature degree) {
        log.info("Down battery temperature");
    }

    public EntityTemperature getCurrentTemperature() {
        log.info("Get current temperature");
        return new EntityTemperature(42.0);
    }

    public void setTemperature(EntityTemperature degree) {
        log.info("Set battery temperature");
    }
}
