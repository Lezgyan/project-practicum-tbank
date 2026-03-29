package ru.tbank.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tbank.practicum.entity.WeatherMeasurement;
import ru.tbank.practicum.service.WeatherService;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/{roomId}")
    public WeatherMeasurement getWeatherByRoomId(@PathVariable Long roomId) {
        return weatherService.getWeatherByRoomId(roomId);
    }
}
