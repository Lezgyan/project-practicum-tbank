package ru.tbank.practicum.controller;

import org.springframework.web.bind.annotation.*;
import ru.tbank.practicum.dto.internal.DtoCoordinate;
import ru.tbank.practicum.dto.internal.DtoWeather;
import ru.tbank.practicum.service.WeatherService;

import java.util.List;

@RestController
@RequestMapping("weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping("/saveWeather")
    public DtoWeather saveCurrentWeather(@RequestBody DtoCoordinate dtoCoordinate){
        return weatherService.getWeatherByCoordinate(dtoCoordinate);
    }

    @GetMapping("/forecasts")
    public List<DtoWeather> getForecasts(){
        return weatherService.getForecasts();
    }

    @GetMapping("/forecasts/{id}")
    public DtoWeather getForecast(@PathVariable Long id){
        return weatherService.getForecast(id);
    }

    @DeleteMapping("/forecasts/{id}")
    public void deleteForecast(@PathVariable Long id){
        weatherService.deleteForecast(id);
    }

}
