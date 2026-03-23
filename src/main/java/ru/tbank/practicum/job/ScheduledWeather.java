package ru.tbank.practicum.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tbank.practicum.config.WeatherServiceProperties;
import ru.tbank.practicum.dto.internal.DtoCoordinate;
import ru.tbank.practicum.dto.internal.DtoWeather;
import ru.tbank.practicum.service.WeatherService;

@Slf4j
@RequiredArgsConstructor
@Component
public class ScheduledWeather {
    private final WeatherService weatherService;

    private final WeatherServiceProperties weatherServiceProperties;

    @Scheduled(fixedRateString = "${app.weather-service.scheduler.rate}")
    private void reportCurrentWeather() {
        DtoWeather dtoWeather = weatherService.getWeatherByCoordinate(new DtoCoordinate(
                weatherServiceProperties.coordinate().lat(),
                weatherServiceProperties.coordinate().lon()));

        log.info(String.valueOf(dtoWeather));
    }
}
