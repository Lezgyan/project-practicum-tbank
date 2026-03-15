package ru.tbank.practicum.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import ru.tbank.practicum.dto.internal.DtoCoordinate;
import ru.tbank.practicum.dto.internal.DtoWeather;
import ru.tbank.practicum.service.WeatherService;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping("/saveWeather")
    public DtoWeather saveCurrentWeather(@Valid @RequestBody DtoCoordinate dtoCoordinate) {
        return weatherService.getWeatherByCoordinate(dtoCoordinate);
    }

    @GetMapping("/forecasts")
    public List<DtoWeather> getForecasts() {
        return weatherService.getForecasts();
    }

    @GetMapping("/forecasts/{id}")
    public DtoWeather getForecast(@PathVariable Long id) {
        return weatherService.getForecast(id);
    }

    @DeleteMapping("/forecasts/{id}")
    public void deleteForecast(@PathVariable Long id) {
        weatherService.deleteForecast(id);
    }
}
