package ru.tbank.practicum.job;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tbank.practicum.dto.internal.DtoCoordinate;
import ru.tbank.practicum.service.WeatherService;

import java.util.Date;

@Component
public class ScheduledWeather {
    private final WeatherService weatherService;

    public ScheduledWeather(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Scheduled(fixedRate = 50000)
    private void reportCurrentWeather(){
        var k = weatherService.getWeatherByCoordinate(new DtoCoordinate(51.32, 46.00));
        System.out.println(k.description());
        System.out.println(k.temperature());
    }
}
