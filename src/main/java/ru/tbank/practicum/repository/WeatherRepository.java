package ru.tbank.practicum.repository;

import org.springframework.stereotype.Repository;
import ru.tbank.practicum.entity.EntityWeather;


import java.util.List;

@Repository
public class WeatherRepository {

    public List<EntityWeather> getAll() {
        System.out.println("CALL getAll METHOD");
        return null;
    }

    public EntityWeather getById(Long id) {
        System.out.println("CALL getById METHOD");
        return null;
    }

    public void deleteById(Long id) {
        System.out.println("CALL deleteById METHOD");
    }
}
