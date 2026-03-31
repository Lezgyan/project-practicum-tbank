package ru.tbank.practicum.job;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tbank.practicum.service.WeatherService;

@RequiredArgsConstructor
@Component
public class ScheduledWeather {

    private final WeatherService weatherService;

    @Scheduled(fixedRateString = "${app.weather-service.scheduler.rate}")
    private void reportCurrentWeather() {
        try {
            weatherService.processRooms();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
