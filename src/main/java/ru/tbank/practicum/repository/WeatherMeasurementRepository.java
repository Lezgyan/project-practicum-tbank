package ru.tbank.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tbank.practicum.entity.WeatherMeasurement;

public interface WeatherMeasurementRepository extends JpaRepository<WeatherMeasurement, Long> {}
