package ru.tbank.practicum.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.tbank.practicum.entity.EntityBattery;

@Slf4j
@Repository
public class BatteryRepository {

    public EntityBattery getCurrentTemperature(Long id) {
        log.info("Get current temperature");
        return new EntityBattery(1L, 42.0);
    }

    public void upTemperature(Long id, Double degree) {
        log.info("Up battery temperature");
    }

    public void downTemperature(Long id, Double degree) {
        log.info("Down battery temperature");
    }

    public void setTemperature(Long id, Double degree) {
        log.info("Set battery temperature");
    }
}
