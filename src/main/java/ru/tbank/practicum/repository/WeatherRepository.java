package ru.tbank.practicum.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.tbank.practicum.entity.EntityWeather;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class WeatherRepository {

    public void save(EntityWeather entityWeather) {
        log.info("Save weather entity");
    }

    public List<EntityWeather> getAll() {
        log.info("Call getAll METHOD");
        return new ArrayList<>(List.of(new EntityWeather(1L, 12.0, "sdf", 1, 12.9, 12.0, 12.0)));
    }

    public EntityWeather getById(Long id) {
        log.info("Call getById METHOD");
        return new EntityWeather(1L, 12.0, "sdf", 1, 12.9, 12.0, 12.0);
    }

    public void deleteById(Long id) {
        log.info("Call deleteById METHOD");
    }
}
