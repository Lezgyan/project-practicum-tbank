package ru.tbank.practicum.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tbank.practicum.dto.internal.DtoCoordinate;
import ru.tbank.practicum.dto.internal.DtoWeather;
import ru.tbank.practicum.service.WeatherService;

@Slf4j
@Component
public class ScheduledWeather {
    private final WeatherService weatherService;

    @Value("${app.baseCoordinateLat}")
    private Double lat;

    @Value("${app.baseCoordinateLon}")
    private Double lon;

    public ScheduledWeather(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Scheduled(fixedRateString = "${app.scheduler.rate}")
    private void reportCurrentWeather() {
        DtoWeather dtoWeather = weatherService.getWeatherByCoordinate(new DtoCoordinate(lat, lon));
        log.info(String.valueOf(dtoWeather));
    }
}
